package com.mobbile.paul.di.subcomponent.login

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class LoginModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}