package com.spinoza.shoppinglist.data.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spinoza.shoppinglist.data.model.ShopItemDbModel

@Dao
interface ShopListDao {
    @Query("SELECT * FROM ${ShopItemDbModel.TABLE_NAME}")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Query("SELECT * FROM ${ShopItemDbModel.TABLE_NAME}")
    fun getShopListCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM ${ShopItemDbModel.TABLE_NAME} WHERE id=:shopItemId")
    suspend fun deleteShopItem(shopItemId: Int)

    @Query("DELETE FROM ${ShopItemDbModel.TABLE_NAME} WHERE id=:shopItemId")
    suspend fun deleteShopItemThenReturnNumberOfItemsRemoved(shopItemId: Int): Int

    @Query("SELECT * FROM ${ShopItemDbModel.TABLE_NAME} WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItem(shopItemId: Int): ShopItemDbModel
}