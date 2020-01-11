package com.mobbile.paul.di.subcomponent.entry

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.entries.EntriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class EntryModule {
    @Binds
    @IntoMap
    @ViewModelKey(EntriesViewModel::class)
    abstract fun bindEntriesViewModel(viewModel: EntriesViewModel): ViewModel
}