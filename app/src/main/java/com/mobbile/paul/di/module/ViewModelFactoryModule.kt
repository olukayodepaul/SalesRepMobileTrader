package com.mobbile.paul.di.module

import androidx.lifecycle.ViewModelProvider
import com.mobbile.paul.viewmodel.ViewModeProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactoryModule: ViewModeProviderFactory):
            ViewModelProvider.Factory
}