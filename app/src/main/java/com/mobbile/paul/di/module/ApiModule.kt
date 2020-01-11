package com.mobbile.paul.di.module

import com.mobbile.paul.provider.Api
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    internal fun provideApi(@Named("api") retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

}