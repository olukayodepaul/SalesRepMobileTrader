package com.mobbile.paul.di.module


import com.mobbile.paul.di.subcomponent.attendant.AttendantModule
import com.mobbile.paul.di.subcomponent.attendant.AttendantScope
import com.mobbile.paul.di.subcomponent.details.DetailsModule
import com.mobbile.paul.di.subcomponent.details.DetailsScope
import com.mobbile.paul.di.subcomponent.enrtryhistory.EntryHistoryModule
import com.mobbile.paul.di.subcomponent.enrtryhistory.EntryHistoryScope
import com.mobbile.paul.di.subcomponent.entry.EntryModule
import com.mobbile.paul.di.subcomponent.entry.EntryScope
import com.mobbile.paul.di.subcomponent.login.LoginModule
import com.mobbile.paul.di.subcomponent.login.LoginScope
import com.mobbile.paul.di.subcomponent.mapnewoutlet.MapNewOutletModule
import com.mobbile.paul.di.subcomponent.mapnewoutlet.MapNewOutletScope
import com.mobbile.paul.di.subcomponent.message.MessageModule
import com.mobbile.paul.di.subcomponent.message.MessageScope
import com.mobbile.paul.di.subcomponent.modules.ModulesModule
import com.mobbile.paul.di.subcomponent.modules.ModulesScope
import com.mobbile.paul.di.subcomponent.order.OrderModule
import com.mobbile.paul.di.subcomponent.order.OrderScope
import com.mobbile.paul.di.subcomponent.outletupdate.OutletUpdateModule
import com.mobbile.paul.di.subcomponent.outletupdate.OutletUpdateScope
import com.mobbile.paul.di.subcomponent.salesfragment.FragmentBuilderScope
import com.mobbile.paul.di.subcomponent.salesfragment.SalesFragentViewPager
import com.mobbile.paul.di.subcomponent.salesfragment.commission.CommissionModules
import com.mobbile.paul.di.subcomponent.salesfragment.customers.CustomerModules
import com.mobbile.paul.di.subcomponent.salesfragment.viewpagermodule.ViewPagerModules
import com.mobbile.paul.ui.attendant.Banks
import com.mobbile.paul.ui.attendant.Close
import com.mobbile.paul.ui.attendant.Resumption
import com.mobbile.paul.ui.details.Details
import com.mobbile.paul.ui.entries.Entries
import com.mobbile.paul.ui.entryhistory.EntryHistory
import com.mobbile.paul.ui.login.MainActivity
import com.mobbile.paul.ui.mapoutlet.MapNewOutlet
import com.mobbile.paul.ui.message.UsersList
import com.mobbile.paul.ui.modules.Modules
import com.mobbile.paul.ui.orders.OrderSummary
import com.mobbile.paul.ui.orders.Orders
import com.mobbile.paul.ui.outletupdate.OutletUpdate
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
            CommissionModules::class,
            ViewPagerModules::class
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

    @DetailsScope
    @ContributesAndroidInjector(
        modules = [
            DetailsModule::class
        ]
    )
    abstract fun contributeDetailsModuleAndroidInjector(): Details

    @OutletUpdateScope
    @ContributesAndroidInjector(
        modules = [
            OutletUpdateModule::class
        ]
    )
    abstract fun contributeOutletUpdateModuleAndroidInjector(): OutletUpdate

    @MapNewOutletScope
    @ContributesAndroidInjector(
        modules = [
            MapNewOutletModule::class
        ]
    )
    abstract fun contributeMapNewOutletModuleAndroidInjector(): MapNewOutlet

    @MessageScope
    @ContributesAndroidInjector(
        modules = [
            MessageModule::class
        ]
    )
    abstract fun contributeMessageModuleAndroidInjector(): UsersList

    @OrderScope
    @ContributesAndroidInjector(
        modules = [
            OrderModule::class
        ]
    )
    abstract fun contributeOrderModuleAndroidInjector(): Orders

    @OrderScope
    @ContributesAndroidInjector(
        modules = [
            OrderModule::class
        ]
    )
    abstract fun contributeOrderSummaryModuleAndroidInjector(): OrderSummary

}