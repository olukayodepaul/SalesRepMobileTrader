package com.mobbile.paul.di.subcomponent.message

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.message.MessageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract  class MessageModule {
    @Binds
    @IntoMap
    @ViewModelKey(MessageViewModel::class)
    abstract fun bindLoginViewModel(viewModel: MessageViewModel): ViewModel
}