package com.mobbile.paul.di.subcomponent.salesfragment.viewpagermodule

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.salesviewpagers.ViewPagerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewPagerModules {
    @Binds
    @IntoMap
    @ViewModelKey(ViewPagerViewModel::class)
    abstract fun bindViewPagerViewModel(viewModel: ViewPagerViewModel): ViewModel
}