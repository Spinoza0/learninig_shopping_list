package com.spinoza.shoppinglist.domain.usecases

import com.spinoza.shoppinglist.domain.model.ShopItem
import com.spinoza.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class DeleteShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListRepository.deleteShopItem(shopItem)
    }
}