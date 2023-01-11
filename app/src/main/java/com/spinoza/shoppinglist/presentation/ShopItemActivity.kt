package com.spinoza.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.data.ShopListRepositoryImpl
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.presentation.viewmodel.ShopItemViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.ViewModelFactory

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShopListRepositoryImpl)
        )[ShopItemViewModel::class.java]

        val mode = intent.getStringExtra(EXTRA_MODE)
        val shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_MODE_ADD = "mode_add"
        private const val EXTRA_MODE_EDIT = "mode_edit"

        fun newIntentEdit(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            with(intent) {
                putExtra(EXTRA_MODE, EXTRA_MODE_EDIT)
                putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            }
            return intent
        }

        fun newIntentAdd(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_ADD)
            return intent
        }

    }
}