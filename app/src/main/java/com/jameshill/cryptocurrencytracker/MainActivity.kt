package com.jameshill.cryptocurrencytracker
//Data collected from coingecko APQ

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jameshill.cryptocurrencytracker.Adapter.CoinAdapter
import com.jameshill.cryptocurrencytracker.Common.Common
import com.jameshill.cryptocurrencytracker.Common.Common.MAX_COIN_LOAD
import com.jameshill.cryptocurrencytracker.Interface.ILoadMore
import com.jameshill.cryptocurrencytracker.Model.CoinModel
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), ILoadMore {
    //Declare variables
    internal var items: MutableList<CoinModel> = ArrayList()
    internal lateinit var adapter: CoinAdapter
    internal lateinit var client: OkHttpClient
    internal lateinit var request: Request

    private fun loadFirst10Coin() {
        TODO("Not yet implemented")
    }


    override fun onLoadMore() {
        if (items.size <= MAX_COIN_LOAD)
            loadNext10Coin(items.size)
        else
            Toast.makeText(
                this@MainActivity,
                "Data max is " + Common.MAX_COIN_LOAD,
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun loadNext10Coin(index: Int) {
        client = OkHttpClient()
        request = Request.Builder()
            .url(
                String.format(
                    "https://api.coingecko.com/api/v3/coins/markets?vs_currency=USD&order=market_cap_desc&per_page=100&page=1&sparkline=false&price_change_percentage=1h%2C24h%2C7d",
                    index
                )
            )
            .build()

        client.newCall(request)
            .enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    Log.e("ERROR", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    val gson = Gson()
                    items = gson.fromJson(body, object : TypeToken<List<CoinModel>>() {}.type)
                    runOnUiThread(
                        items.addAll(items)
                                adapter . updateData (items)


                    )
                }
            })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_to_refresh.post { loadFirst10Coin() }
        swipe_to_refresh.setOnRefreshListener {
            items.clear()
            loadFirst10coin()
            setUpAdapter()
        }
        coin_recycler_view.layoutManager = LinearLayoutManager(this)
        setUpAdapter()
    }


    private fun setUpAdapter() {
        adapter = CoinAdapter(coin_recycler_view, this@MainActivity, items)
        coin_recycler_view.adapter = adapter
        adapter.setLoadMore(this)
    }


}