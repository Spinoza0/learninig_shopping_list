package com.spinoza.shoppinglist.data

import com.spinoza.shoppinglist.domain.ShopItem

class ShopListMapper {
    fun mapEntityToDBModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enabled = shopItem.enabled
    )

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enabled = shopItemDbModel.enabled
    )

    fun mapListDbModelToListEntity(listDbModel: List<ShopItemDbModel>) = listDbModel.map {
        mapDbModelToEntity(it)
    }
}