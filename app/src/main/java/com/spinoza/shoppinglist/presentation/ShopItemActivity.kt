package com.spinoza.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.data.ShopListRepositoryImpl
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.presentation.viewmodel.ShopItemViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.ViewModelFactory

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var editTextName: TextInputEditText
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var editTextCount: TextInputEditText
    private lateinit var buttonSave: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        initViews()

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
        viewModel.shouldCloseScreen.observe(this) { finish() }
        viewModel.shopItem.observe(this) {
            editTextName.setText(it.name)
            editTextCount.setText(it.count.toString())
        }
        viewModel.errorInputName.observe(this) {
            textInputLayoutName.error = if (it) getString(R.string.error_input_name) else null

        }
        viewModel.errorInputCount.observe(this) {
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

    private fun parseIntent() {
        if (intent.hasExtra(EXTRA_SCREEN_MODE)) {
            val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
            if (mode == MODE_ADD || mode == MODE_EDIT) {
                screenMode = mode
                if (screenMode == MODE_EDIT) {
                    if (intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                        shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
                    } else throw RuntimeException("Param shop item id is absent")
                }
            } else throw RuntimeException("Unknown screen mode $mode")
        } else throw RuntimeException("Param screen mode is absent")
    }

    private fun initViews() {
        textInputLayoutName = findViewById(R.id.textInputLayoutName)
        editTextName = findViewById(R.id.editTextName)
        textInputLayoutCount = findViewById(R.id.textInputLayoutCount)
        editTextCount = findViewById(R.id.editTextCount)
        buttonSave = findViewById(R.id.buttonSave)
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            with(intent) {
                putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
                putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            }
            return intent
        }

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

    }
}