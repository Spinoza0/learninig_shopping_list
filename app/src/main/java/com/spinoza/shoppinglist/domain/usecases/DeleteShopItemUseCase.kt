package com.spinoza.shoppinglist.domain.usecases

import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.domain.ShopListRepository

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)
    }
}