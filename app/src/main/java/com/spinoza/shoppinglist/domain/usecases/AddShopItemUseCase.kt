package com.spinoza.shoppinglist.domain.usecases

import com.spinoza.shoppinglist.domain.model.ShopItem
import com.spinoza.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    suspend fun addShopItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }
}