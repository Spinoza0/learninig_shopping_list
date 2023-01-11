package com.spinoza.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.data.ShopListRepositoryImpl
import com.spinoza.shoppinglist.presentation.adapter.ShopListAdapter
import com.spinoza.shoppinglist.presentation.viewmodel.MainViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var recyclerViewShopList: RecyclerView
    private lateinit var buttonAddShopItem: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupListeners()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShopListRepositoryImpl)
        )[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }
    }

    private fun initViews() {
        recyclerViewShopList = findViewById(R.id.recyclerViewShopList)
        buttonAddShopItem = findViewById(R.id.buttonAddShopItem)
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

        shopListAdapter.onShopItemClickListener = {
            startActivity(ShopItemActivity.newIntentEdit(this, it.id))
        }

        buttonAddShopItem.setOnClickListener {
            startActivity(ShopItemActivity.newIntentAdd(this))
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
}