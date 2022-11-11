package com.example.battleshipmobile.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
fun <T: ViewModel> viewModelInit(block: () -> T) = object: ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return block() as T
    }
}