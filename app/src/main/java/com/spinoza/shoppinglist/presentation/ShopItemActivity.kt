package com.spinoza.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.domain.ShopItem
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.MODE_ADD
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.MODE_EDIT
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.MODE_UNKNOWN
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.SCREEN_MODE
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment.Companion.SHOP_ITEM_ID

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID
    private var shopItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseParams()
        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
        startActivity(
            if (screenMode == MODE_ADD) MainActivity.newIntentMoveToEnd(this)
            else MainActivity.newIntentRestorePosition(this, shopItemPosition)
        )
        finish()
    }

    // TODO: fix it later
    override fun onBackPressed() {
        startActivity(MainActivity.newIntentRestorePosition(this, shopItemPosition))
        finish()
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .commit()
    }

    private fun parseParams() {
        if (intent.hasExtra(SCREEN_MODE)) {
            val mode = intent.getStringExtra(SCREEN_MODE)
            if (mode == MODE_ADD || mode == MODE_EDIT) {
                shopItemPosition = intent.getIntExtra(FIRST_VISIBLE_POSITION, 0)
                screenMode = mode
                if (screenMode == MODE_EDIT) {
                    if (intent.hasExtra(SHOP_ITEM_ID)) {
                        shopItemId = intent.getIntExtra(SHOP_ITEM_ID,
                            ShopItem.UNDEFINED_ID)
                    } else throw RuntimeException("Param shop item id is absent")
                }
            } else throw RuntimeException("Unknown screen mode $mode")
        } else throw RuntimeException("Param screen mode is absent")
    }

    companion object {
        private const val FIRST_VISIBLE_POSITION = "position"
        fun newIntentEdit(context: Context, shopItemId: Int, firstVisiblePosition: Int): Intent {
            return Intent(context, ShopItemActivity::class.java).apply {
                putExtra(SCREEN_MODE, MODE_EDIT)
                putExtra(SHOP_ITEM_ID, shopItemId)
                putExtra(FIRST_VISIBLE_POSITION, firstVisiblePosition)
            }
        }

        fun newIntentAdd(context: Context, firstVisiblePosition: Int): Intent {
            return Intent(context, ShopItemActivity::class.java).apply {
                putExtra(SCREEN_MODE, MODE_ADD)
                putExtra(FIRST_VISIBLE_POSITION, firstVisiblePosition)
            }
        }
    }
}