package com.spinoza.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.EXTRA_SCREEN_MODE
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.EXTRA_SHOP_ITEM_ID
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.MODE_ADD
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.MODE_EDIT
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.MODE_UNKNOWN

class ShopItemActivity : AppCompatActivity() {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseParams()
        launchRightMode()
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.shopItemContainer, fragment)
            .commit()
    }

    private fun parseParams() {
        if (intent.hasExtra(EXTRA_SCREEN_MODE)) {
            val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
            if (mode == MODE_ADD || mode == MODE_EDIT) {
                screenMode = mode
                if (screenMode == MODE_EDIT) {
                    if (intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                        shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID,
                            ShopItem.UNDEFINED_ID)
                    } else throw RuntimeException("Param shop item id is absent")
                }
            } else throw RuntimeException("Unknown screen mode $mode")
        } else throw RuntimeException("Param screen mode is absent")
    }

    companion object {
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