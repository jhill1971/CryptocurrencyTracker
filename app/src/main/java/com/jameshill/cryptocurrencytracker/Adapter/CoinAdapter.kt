package com.jameshill.cryptocurrencytracker.Adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jameshill.cryptocurrencytracker.Common.Common
import com.jameshill.cryptocurrencytracker.Interface.ILoadMore
import com.jameshill.cryptocurrencytracker.Model.CoinModel
import com.jameshill.cryptocurrencytracker.R
import com.squareup.picasso.Picasso

class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coinSymbol
    var coinName = itemView.coinName
    var coinPrice = itemView.priceUSD
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDay
}

class CoinAdapter(
    recyclerView: RecyclerView,
    internal var activity: Activity,
    var items: List<CoinModel>
) : RecyclerView.Adapter<CoinViewHolder>() {

    var loadMore: ILoadMore? = null
    var isLoading: Boolean = false
    var visibleThreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }

            }
        })
    }

    @JvmName("setLoadMore1")
    fun setLoadMore(loadMore: ILoadMore) {
        this.loadMore = loadMore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.coin_layout, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coinModel = items.get(position)
        val item = holder as CoinViewHolder

        item.coinName = coinModel.name
        item.coinSymbol = coinModel.symbol
        item.coinPrice = coinModel.current_price
        item.oneHourChange = coinModel.price_change_percentage_1h_in_currency + "%"
        item.twentyFourChange = coinModel.price_change_percentage_24h_in_currency + "%"
        item.sevenDayChange = coinModel.price_change_percentage_7d_in_currency + "%"

        Picasso.get()
            .load(
                StringBuilder(Common.image)
                    .append(coinModel.symbol!!.toLowerCase())

                    .append(".png")
                    .toString()  )
            .into (item.coinIcon)

        //set Color
        item.oneHourChange.setTextColor
        (if (coinModel.price_change_percentage_1h_in_currency!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
                )

        item.twentyFourChange.setTextColor
        (if (coinModel.price_change_percentage_24h_in_currency!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
                )

        item.sevenDayChange.setTextColor
        (if (coinModel.price_change_percentage_7d_in_currency!!.contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
                )

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setLoaded() {
        isLoading = false
    }

    fun updateData(coinModel: List<CoinModel>) {
        this.items = coinModel
        notifyDataSetChanged()
    }

}