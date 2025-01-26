package com.example.pinspire.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is the Search Fragment"
    }
    val text = _text
}