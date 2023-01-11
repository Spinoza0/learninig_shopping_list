package com.spinoza.shoppinglist.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.data.ShopListRepositoryImpl
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.presentation.viewmodel.ShopItemViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.ViewModelFactory

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID,
) : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var editTextName: TextInputEditText
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var editTextCount: TextInputEditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parseParams()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShopListRepositoryImpl)
        )[ShopItemViewModel::class.java]

        initViews(view)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShopListRepositoryImpl)
        )[ShopItemViewModel::class.java]

        setupScreen()
        launchRightMode()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        buttonSave.setOnClickListener {
            viewModel.editShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun setupScreen() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            // TODO: fix it later
            activity?.onBackPressed()
        }
        viewModel.shopItem.observe(viewLifecycleOwner) {
            editTextName.setText(it.name)
            editTextCount.setText(it.count.toString())
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            textInputLayoutName.error = if (it) getString(R.string.error_input_name) else null

        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            textInputLayoutCount.error = if (it) getString(R.string.error_input_count) else null
        }

        setTextChangeListeners()
    }

    private fun setTextChangeListeners() {
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


    private fun initViews(view: View) {
        with(view) {
            textInputLayoutName = findViewById(R.id.textInputLayoutName)
            editTextName = findViewById(R.id.editTextName)
            textInputLayoutCount = findViewById(R.id.textInputLayoutCount)
            editTextCount = findViewById(R.id.editTextCount)
            buttonSave = findViewById(R.id.buttonSave)
        }
    }


    private fun parseParams() {
        if (screenMode == MODE_ADD || screenMode == MODE_EDIT) {
            if (screenMode == MODE_EDIT && shopItemId == ShopItem.UNDEFINED_ID) {
                throw RuntimeException("Param shop item id is absent")
            }
        } else throw RuntimeException("Param screen mode is absent or unknown")
    }

    companion object {
        const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        const val EXTRA_SCREEN_MODE = "extra_mode"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }
    }
}