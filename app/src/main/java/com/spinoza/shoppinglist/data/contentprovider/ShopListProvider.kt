package com.spinoza.shoppinglist.data.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.spinoza.shoppinglist.data.database.ShopListDao
import com.spinoza.shoppinglist.data.mapper.ShopListMapper
import com.spinoza.shoppinglist.data.model.ShopItemDbModel
import com.spinoza.shoppinglist.domain.model.ShopItem
import com.spinoza.shoppinglist.presentation.ShopListApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopListProvider : ContentProvider() {

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val component by lazy {
        (context as ShopListApp).component
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH, GET_SHOP_ITEMS_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? = when (uriMatcher.match(uri)) {
        GET_SHOP_ITEMS_QUERY -> {
            shopListDao.getShopListCursor()
        }
        else -> null
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                if (values != null) {
                    val id = values.getAsInteger(ShopItemDbModel.ID)
                    val name = values.getAsString(ShopItemDbModel.NAME)
                    val count = values.getAsFloat(ShopItemDbModel.COUNT)
                    val enabled = values.getAsBoolean(ShopItemDbModel.ENABLED)
                    val shopItem = ShopItem(name, count, enabled, id)
                    CoroutineScope(Dispatchers.IO).launch {
                        shopListDao.addShopItem(mapper.mapEntityToDBModel(shopItem))
                    }
                }
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val PATH = "shop_items/#"                     // # - number
        private const val PATH2 = "shop_items/*"                    // * - string
        private const val AUTHORITY = "com.spinoza.shoppinglist"    // see manifest
        private const val GET_SHOP_ITEMS_QUERY = 100                // random number
    }
}