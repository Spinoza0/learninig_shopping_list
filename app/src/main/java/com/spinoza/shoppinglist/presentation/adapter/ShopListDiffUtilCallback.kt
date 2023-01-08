package com.spinoza.shoppinglist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.spinoza.shoppinglist.domain.ShopItem

class ShopListDiffUtilCallback(
    private var oldList: List<ShopItem>,
    private var newList: List<ShopItem>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.enabled == newItem.enabled &&
                oldItem.count == newItem.count &&
                oldItem.name == newItem.name
    }
}