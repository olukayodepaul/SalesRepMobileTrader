package com.mobbile.paul.di.subcomponent.enrtryhistory

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.entryhistory.EntryHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class EntryHistoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(EntryHistoryViewModel::class)
    abstract fun bindEntryHistoryViewModel(viewModel: EntryHistoryViewModel): ViewModel
}