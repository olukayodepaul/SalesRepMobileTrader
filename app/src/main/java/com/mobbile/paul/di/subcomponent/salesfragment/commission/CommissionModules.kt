package com.mobbile.paul.di.subcomponent.salesfragment.commission

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.comission.CommissionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CommissionModules {
    @Binds
    @IntoMap
    @ViewModelKey(CommissionViewModel::class)
    abstract fun bindCustomerCommissionModules(viewModel: CommissionViewModel): ViewModel
}