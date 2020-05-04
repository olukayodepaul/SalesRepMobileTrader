package com.mobbile.paul.ui.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.modulesEntity
import com.mobbile.paul.provider.Repository
import javax.inject.Inject

class ModulesViewModel @Inject constructor(private var repository: Repository): ViewModel() {

    //i do not persist modules
    fun getModules() : LiveData<List<modulesEntity>> {
        val result = MutableLiveData<List<modulesEntity>>()
        repository.fetchModules()
            .subscribe({
                result.postValue(it)
            },{
                result.postValue(emptyList())
            }).isDisposed
        return result
    }

    //count for the message and link the message url direct to messages
    fun countUnReadMessage(): LiveData<Int> {
        val nts = MutableLiveData<Int>()
        repository.countUnReadMessage().subscribe(
            {
                nts.postValue(it)
            },{
                nts.postValue(0)
            }
        ).isDisposed
        return nts
    }


}