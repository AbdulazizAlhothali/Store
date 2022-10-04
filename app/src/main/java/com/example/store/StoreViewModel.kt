package com.example.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class StoreViewModel: ViewModel() {
    var clearCart = MutableLiveData(false)

    var products = MutableLiveData<List<Product>>()
    var currency = MutableLiveData<Currency>()
    var orderTotal = MutableLiveData(0.00)

    fun calculateOrderTotal(){
        val basket = products.value?.filter { p ->
            p.inCart
        } ?: listOf()
        var total = 0.00
        for (p in basket) total += p.price
        if (currency.value != null) total *= currency.value?.exchangeRate ?: 1.00
// Use BigDecimal to round the orderTotal value to two decimal places
        orderTotal.value = BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

}