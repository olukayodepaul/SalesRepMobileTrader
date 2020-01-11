package com.mobbile.paul


import com.jakewharton.threetenabp.AndroidThreeTen
import com.mobbile.paul.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        AndroidThreeTen.init(this)
        return DaggerAppComponent.builder().application(this).build()
    }
}
