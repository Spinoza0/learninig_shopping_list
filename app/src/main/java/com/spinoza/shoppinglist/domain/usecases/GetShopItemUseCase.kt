package com.spinoza.shoppinglist.domain.usecases

import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.domain.ShopListRepository

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun getShopItem(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}