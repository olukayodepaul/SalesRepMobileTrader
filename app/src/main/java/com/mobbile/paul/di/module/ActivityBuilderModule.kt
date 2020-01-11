package com.mobbile.paul.di.module


import com.mobbile.paul.di.subcomponent.attendant.AttendantModule
import com.mobbile.paul.di.subcomponent.attendant.AttendantScope
import com.mobbile.paul.di.subcomponent.enrtryhistory.EntryHistoryModule
import com.mobbile.paul.di.subcomponent.enrtryhistory.EntryHistoryScope
import com.mobbile.paul.di.subcomponent.entry.EntryModule
import com.mobbile.paul.di.subcomponent.entry.EntryScope
import com.mobbile.paul.di.subcomponent.login.LoginModule
import com.mobbile.paul.di.subcomponent.login.LoginScope
import com.mobbile.paul.di.subcomponent.modules.ModulesModule
import com.mobbile.paul.di.subcomponent.modules.ModulesScope
import com.mobbile.paul.di.subcomponent.salesfragment.FragmentBuilderScope
import com.mobbile.paul.di.subcomponent.salesfragment.SalesFragentViewPager
import com.mobbile.paul.di.subcomponent.salesfragment.commission.CommissionModules
import com.mobbile.paul.di.subcomponent.salesfragment.customers.CustomerModules
import com.mobbile.paul.ui.attendant.Banks
import com.mobbile.paul.ui.attendant.Close
import com.mobbile.paul.ui.attendant.Resumption
import com.mobbile.paul.ui.entries.Entries
import com.mobbile.paul.ui.entryhistory.EntryHistory
import com.mobbile.paul.ui.login.MainActivity
import com.mobbile.paul.ui.modules.Modules
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @LoginScope
    @ContributesAndroidInjector(
        modules = [
            LoginModule::class
        ]
    )
    abstract fun contributeActivityAndroidInjector(): MainActivity

    @ModulesScope
    @ContributesAndroidInjector(
        modules = [
            ModulesModule::class
        ]
    )
    abstract fun contributeModulesModuleAndroidInjector(): Modules

    @FragmentBuilderScope
    @ContributesAndroidInjector(
        modules = [
            SalesFragentViewPager::class,
            CustomerModules::class,
            CommissionModules::class
        ]
    )
    abstract fun contributeSalesPagerActivity(): SalesViewPager


    @EntryScope
    @ContributesAndroidInjector(
        modules = [
            EntryModule::class
        ]
    )
    abstract fun contributeEntryModuleAndroidInjector(): Entries

    @EntryHistoryScope
    @ContributesAndroidInjector(
        modules = [
            EntryHistoryModule::class
        ]
    )
    abstract fun contributeEntryHistoryModuleAndroidInjector(): EntryHistory


    @AttendantScope
    @ContributesAndroidInjector(
        modules = [
            AttendantModule::class
        ]
    )
    abstract fun contributeBanksAndroidInjector(): Banks

    @AttendantScope
    @ContributesAndroidInjector(
        modules = [
            AttendantModule::class
        ]
    )
    abstract fun contributeCloseAndroidInjector(): Close

    @AttendantScope
    @ContributesAndroidInjector(
        modules = [
            AttendantModule::class
        ]
    )
    abstract fun contributeResumptionAndroidInjector(): Resumption

}