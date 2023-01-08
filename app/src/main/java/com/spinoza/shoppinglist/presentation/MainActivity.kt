package com.spinoza.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.spinoza.shoppinglist.R
import com.spinoza.shoppinglist.data.ShopListRepositoryImpl
import com.spinoza.shoppinglist.presentation.adapter.ShopListAdapter
import com.spinoza.shoppinglist.presentation.viewmodel.MainViewModel
import com.spinoza.shoppinglist.presentation.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(ShopListRepositoryImpl)
        )[MainViewModel::class.java]

        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
        }
    }

    private fun setupRecyclerView() {
        with(findViewById<RecyclerView>(R.id.recyclerViewShopList)) {
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
        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }
    }
}