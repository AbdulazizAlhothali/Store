package com.example.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoreViewModel: ViewModel() {
    var products = MutableLiveData<List<Product>>()
    var currency = MutableLiveData<Currency>()

}