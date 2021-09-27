package com.jameshill.cryptocurrencytracker.Model

class CoinModel {
    var id: String? = null
    var name: String? = null
    var symbol: String? = null
    var image: String? = null
    var current_price: String? = null
    var price_change_percentage_1h_in_currency: String? = null
    var price_change_percentage_24h_in_currency: String? = null
    var price_change_percentage_7d_in_currency: String? = null

    constructor()
    constructor(
        id: String,
        name: String,
        symbol: String?,
        image: String?,
        current_price: String,
        price_change_percentage_1h_in_currency: String,
        price_change_percentage_24h_in_currency: String,
        price_change_percentage_7d_in_currency: String
    ) {
        this.id = id
        this.name = name
        this.symbol = symbol
        this.image = image
        this.current_price = current_price
        this.price_change_percentage_1h_in_currency = price_change_percentage_1h_in_currency
        this.price_change_percentage_24h_in_currency = price_change_percentage_24h_in_currency
        this.price_change_percentage_7d_in_currency = price_change_percentage_7d_in_currency
    }

}