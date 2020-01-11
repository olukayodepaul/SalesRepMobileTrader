package com.mobbile.paul.di.subcomponent.salesfragment

import com.mobbile.paul.ui.comission.Comission
import com.mobbile.paul.ui.customers.Customers
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SalesFragentViewPager {

    @ContributesAndroidInjector
    abstract fun contributeCustomersFragment(): Customers

    @ContributesAndroidInjector
    abstract fun contributeComissionFragment(): Comission

}