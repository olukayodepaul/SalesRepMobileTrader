package com.mobbile.paul.ui.comission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.salesCommissionList
import com.mobbile.paul.provider.Repository
import javax.inject.Inject

class CommissionViewModel @Inject constructor(private var repository: Repository):ViewModel() {

    fun salesCommission(user_id: Int) : LiveData<List<salesCommissionList>> {

        val mResult = MutableLiveData<List<salesCommissionList>>()
        repository.salesCommission(user_id)
            .subscribe({
                if(it.body()!!.status==200){
                    mResult.postValue(it.body()!!.comlist)
                }else{
                    mResult.postValue(null)
                }
            },{
                mResult.postValue(null)
            }).isDisposed
        return mResult
    }

}