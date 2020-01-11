package com.mobbile.paul.di.subcomponent.modules

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.modules.ModulesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class ModulesModule {
    @Binds
    @IntoMap
    @ViewModelKey(ModulesViewModel::class)
    abstract fun bindModulesViewModel(viewModel: ModulesViewModel): ViewModel
}