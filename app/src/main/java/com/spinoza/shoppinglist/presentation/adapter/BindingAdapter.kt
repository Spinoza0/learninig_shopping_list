package com.spinoza.shoppinglist.presentation.adapter

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.spinoza.shoppinglist.R

interface TextChangeListener {
    operator fun invoke()
}

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

@BindingAdapter("textChangeListener")
fun bindTextChangeListener(
    textInputEditText: TextInputEditText,
    textChangeListener: TextChangeListener,
) {
    textInputEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textChangeListener()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}