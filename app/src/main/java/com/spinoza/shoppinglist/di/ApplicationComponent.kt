package com.spinoza.shoppinglist.di

import android.app.Application
import com.spinoza.cryptoapp.di.ViewModelModule
import com.spinoza.shoppinglist.presentation.MainActivity
import com.spinoza.shoppinglist.presentation.fragment.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}