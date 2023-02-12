package com.spinoza.shoppinglist.di

import android.app.Application
import com.spinoza.shoppinglist.data.database.AppDatabase
import com.spinoza.shoppinglist.data.database.ShopListDao
import com.spinoza.shoppinglist.data.repository.ShopListRepositoryImpl
import com.spinoza.shoppinglist.domain.repository.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideShopListDao(application: Application): ShopListDao =
            AppDatabase.getInstance(application).shopListDao()

    }
}