package com.spinoza.shoppinglist.domain

data class ShopItem(
    val name: String,
    val count: Float,
    var enabled: Boolean,
    var id: Int = UNDEFINED_ID,
) : java.io.Serializable {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
