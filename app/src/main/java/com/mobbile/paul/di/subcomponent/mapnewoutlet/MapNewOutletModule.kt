package com.mobbile.paul.di.subcomponent.mapnewoutlet

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.mapoutlet.MapNewOutletViewModel
import com.mobbile.paul.ui.outletupdate.OutletUpdateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class MapNewOutletModule {
    @Binds
    @IntoMap
    @ViewModelKey(MapNewOutletViewModel::class)
    abstract fun bindMapNewOutletModule(viewModel: MapNewOutletViewModel): ViewModel
}