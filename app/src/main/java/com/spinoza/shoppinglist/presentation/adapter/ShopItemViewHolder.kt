package com.spinoza.shoppinglist.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spinoza.shoppinglist.R

class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val textViewName: TextView = view.findViewById(R.id.textViewName)
    val textViewCount: TextView = view.findViewById(R.id.textViewCount)
}