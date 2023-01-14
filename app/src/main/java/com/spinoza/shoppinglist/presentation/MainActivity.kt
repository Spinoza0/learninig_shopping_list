package com.spinoza.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.data.ShopListRepositoryImpl
import com.spinoza.shoppinglist.presentation.adapter.ShopListAdapter
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment
import com.spinoza.shoppinglist.presentation.viewmodel.MainViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var recyclerViewShopList: RecyclerView
    private lateinit var buttonAddShopItem: FloatingActionButton
    private var shopItemContainer: FragmentContainerView? = null
    private var needMoveToLastPosition = false
    private var needRestorePosition = false
    private var firstVisiblePosition = 0
    private var modeAdd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShopListRepositoryImpl)
        )[MainViewModel::class.java]

        if (intent.hasExtra(MOVE_MODE)) {
            when (intent.getStringExtra(MOVE_MODE)) {
                MODE_LAST_POSITION -> needMoveToLastPosition = true
                MODE_RESTORE_POSITION -> if (isOnePanelMode()) {
                    needRestorePosition = true
                    firstVisiblePosition = intent.getIntExtra(
                        FIRST_VISIBLE_POSITION,
                        DEFAULT_VISIBLE_POSITION
                    )
                }
            }
        }

        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it) {
                if (needMoveToLastPosition) {
                    recyclerViewShopList.scrollToPosition(shopListAdapter.itemCount - 1)
                    needMoveToLastPosition = false
                } else if (needRestorePosition) {
                    recyclerViewShopList.scrollToPosition(firstVisiblePosition)
                }
            }
        }
    }

    private fun initViews() {
        recyclerViewShopList = findViewById(R.id.recyclerViewShopList)
        buttonAddShopItem = findViewById(R.id.buttonAddShopItem)
        shopItemContainer = findViewById(R.id.shopItemContainer)
    }

    private fun setupRecyclerView() {
        with(recyclerViewShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
    }

    private fun setupListeners() {
        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }

        shopListAdapter.onShopItemClickListener = { shopItem, shopItemPosition ->
            editShopItem(shopItem.id, getFirstVisiblePosition(shopItemPosition))
        }

        buttonAddShopItem.setOnClickListener {
            addShopItem(getFirstVisiblePosition(DEFAULT_VISIBLE_POSITION))
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteShopItem(shopListAdapter.currentList[viewHolder.adapterPosition])
            }
        }).attachToRecyclerView(recyclerViewShopList)
    }

    private fun getFirstVisiblePosition(default: Int): Int {
        return if (recyclerViewShopList.layoutManager is LinearLayoutManager) {
            (recyclerViewShopList.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
        } else {
            default
        }
    }

    private fun isOnePanelMode(): Boolean = shopItemContainer == null

    private fun addShopItem(shopItemPosition: Int) {
        if (isOnePanelMode()) {
            startActivity(ShopItemActivity.newIntentAdd(this, shopItemPosition))
            finish()
        } else {
            modeAdd = true
            launchFragment(ShopItemFragment.newInstanceAddItem())
        }
    }

    private fun editShopItem(shopItemId: Int, shopItemPosition: Int) {
        if (isOnePanelMode()) {
            startActivity(
                ShopItemActivity.newIntentEdit(this, shopItemId, shopItemPosition)
            )
            finish()
        } else {
            launchFragment(ShopItemFragment.newInstanceEditItem(shopItemId))
        }
    }

    private fun launchFragment(shopItemFragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemContainer, shopItemFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
        needMoveToLastPosition = modeAdd
        modeAdd = false
    }

    companion object {
        private const val MOVE_MODE = "move_mode"
        private const val MODE_LAST_POSITION = "last"
        private const val MODE_RESTORE_POSITION = "restore"
        private const val FIRST_VISIBLE_POSITION = "position"
        private const val DEFAULT_VISIBLE_POSITION = 0

        fun newIntentRestorePosition(context: Context, firstVisiblePosition: Int): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(MOVE_MODE, MODE_RESTORE_POSITION)
                putExtra(FIRST_VISIBLE_POSITION, firstVisiblePosition)
            }
        }

        fun newIntentMoveToEnd(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(MOVE_MODE, MODE_LAST_POSITION)
            }
        }
    }
}