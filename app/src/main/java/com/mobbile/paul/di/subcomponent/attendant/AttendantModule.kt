package com.mobbile.paul.di.subcomponent.attendant

import androidx.lifecycle.ViewModel
import com.mobbile.paul.provider.ViewModelKey
import com.mobbile.paul.ui.attendant.AttendantViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract  class AttendantModule {
    @Binds
    @IntoMap
    @ViewModelKey(AttendantViewModel::class)
    abstract fun bindAttendantViewModel(viewModel: AttendantViewModel): ViewModel
}