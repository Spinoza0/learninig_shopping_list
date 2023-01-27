package com.spinoza.shoppinglist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.domain.ShopListRepository
import com.spinoza.shoppinglist.domain.usecases.DeleteShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.EditShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.GetShopListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(repository: ShopListRepository) : ViewModel() {

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val scope = CoroutineScope(Dispatchers.Main)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem) {
        scope.launch {
            val newItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}