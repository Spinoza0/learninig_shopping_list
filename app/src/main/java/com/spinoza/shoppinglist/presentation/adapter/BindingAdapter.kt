package com.spinoza.shoppinglist.presentation.adapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.spinoza.shoppinglist.R

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    textInputLayout.error =
        if (isError) textInputLayout.context.getString(R.string.error_input_name) else null
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    textInputLayout.error =
        if (isError) textInputLayout.context.getString(R.string.error_input_count) else null
}