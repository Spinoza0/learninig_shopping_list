package com.spinoza.shoppinglist.domain.usecases

import androidx.lifecycle.LiveData
import com.spinoza.shoppinglist.domain.model.ShopItem
import com.spinoza.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class GetShopListUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> {
        return shopListRepository.getShopList()
    }
}