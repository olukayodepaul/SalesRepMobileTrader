package com.mobbile.paul.di.subcomponent.outletupdate

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.outletupdate.OutletUpdateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class OutletUpdateModule {
    @Binds
    @IntoMap
    @ViewModelKey(OutletUpdateViewModel::class)
    abstract fun bindOutletUpdateViewModel(viewModel: OutletUpdateViewModel): ViewModel
}