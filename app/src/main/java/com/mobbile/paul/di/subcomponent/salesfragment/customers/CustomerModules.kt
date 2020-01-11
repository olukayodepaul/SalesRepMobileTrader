package com.mobbile.paul.di.subcomponent.salesfragment.customers

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.customers.CustomerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CustomerModules {
    @Binds
    @IntoMap
    @ViewModelKey(CustomerViewModel::class)
    abstract fun bindCustomerCustomer(viewModel: CustomerViewModel): ViewModel
}