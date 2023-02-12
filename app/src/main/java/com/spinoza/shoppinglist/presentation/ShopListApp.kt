package com.spinoza.shoppinglist.presentation

import android.app.Application
import com.spinoza.shoppinglist.di.DaggerApplicationComponent

class ShopListApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}