package com.spinoza.shoppinglist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spinoza.shoppinglist.domain.ShopListRepository

class ViewModelFactory(private val repository: ShopListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(ShopListRepository::class.java)
            .newInstance(repository)
    }
}