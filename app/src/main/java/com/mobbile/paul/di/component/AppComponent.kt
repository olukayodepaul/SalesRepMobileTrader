package com.mobbile.paul.di.component

import android.app.Application
import com.mobbile.paul.BaseApplication
import com.mobbile.paul.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
    ViewModelFactoryModule::class,
    LocalCacheModule::class,
    ApiModule::class,
    NetworkModule::class
])
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
