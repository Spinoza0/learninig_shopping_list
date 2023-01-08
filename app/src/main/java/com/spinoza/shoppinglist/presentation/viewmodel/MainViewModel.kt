package com.spinoza.shoppinglist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.domain.ShopListRepository
import com.spinoza.shoppinglist.domain.usecases.DeleteShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.EditShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.GetShopListUseCase

class MainViewModel(repository: ShopListRepository) : ViewModel() {

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}