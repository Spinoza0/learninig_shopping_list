package com.spinoza.shoppinglist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spinoza.shoppinglist.domain.model.ShopItem
import com.spinoza.shoppinglist.domain.usecases.AddShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.EditShopItemUseCase
import com.spinoza.shoppinglist.domain.usecases.GetShopItemUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase: GetShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
) : ViewModel() {

    private var editMode = false

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        editMode = true
        viewModelScope.launch {
            _shopItem.value = getShopItemUseCase.getShopItem(shopItemId)
        }
    }

    fun saveShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        if (validateInput(name, count)) {
            viewModelScope.launch {
                if (editMode) {
                    _shopItem.value?.let {
                        val shopItem = it.copy(name = name, count = count)
                        editShopItemUseCase.editShopItem(shopItem)
                        finishWork()
                    }
                } else {
                    val shopItem = ShopItem(name, count, true)
                    addShopItemUseCase.addShopItem(shopItem)
                    finishWork()
                }
            }
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
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }

        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}