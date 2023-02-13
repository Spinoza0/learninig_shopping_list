package com.spinoza.shoppinglist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ShopItemDbModel.TABLE_NAME)
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val count: Float,
    var enabled: Boolean,
) {
    companion object {
        const val TABLE_NAME = "shop_items"
        const val NAME = "name"
        const val COUNT = "count"
        const val ENABLED = "enabled"
        const val ID = "id"
    }
}