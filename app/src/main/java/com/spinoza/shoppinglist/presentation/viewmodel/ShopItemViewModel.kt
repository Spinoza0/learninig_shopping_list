package com.spinoza.shoppinglist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.domain.ShopListRepository
import com.spinoza.shoppinglist.domain.usecases.AddShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.EditShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.GetShopItemUseCase

class ShopItemViewModel(repository: ShopListRepository) : ViewModel() {
    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int): ShopItem = getShopItemUseCase.getShopItem(shopItemId)

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
        }
    }

    fun editShopItem(shopItem: ShopItem, inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            val newShopItem = ShopItem(name, count, shopItem.enabled, shopItem.id)
            editShopItemUseCase.editShopItem(newShopItem)
        }
    }

    private fun parseName(inputName: String?): String = inputName?.trim() ?: ""

    private fun parseCount(inputCount: String?): Float = try {
        inputCount?.trim()?.toFloat() ?: 0f
    } catch (e: Exception) {
        0f
    }

    private fun validateInput(name: String, count: Float): Boolean {
        var result = true
        if (name.isBlank()) {
            // TODO: show error input name
            result = false
        }
        if (count <= 0) {
            // TODO: show error input count
            result = false
        }

        return result
    }
}