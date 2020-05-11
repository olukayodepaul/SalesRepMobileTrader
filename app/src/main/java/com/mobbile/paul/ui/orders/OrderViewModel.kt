package com.mobbile.paul.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.model.allskuOrderd
import com.mobbile.paul.provider.Repository
import javax.inject.Inject

class OrderViewModel @Inject constructor(private var repository: Repository): ViewModel() {

    fun getcustomerOrder(employeeid:Int) : LiveData<List<allCustomerProductOrder>> {
        val result = MutableLiveData<List<allCustomerProductOrder>>()
        repository.customerOrder(employeeid)
            .subscribe({
                result.postValue(it.body()!!.order)
            },{
                result.postValue(emptyList())
            }).isDisposed
        return result
    }

    fun getskuTotalOrder(orderInfoId:Int) : LiveData<List<allskuOrderd>> {
        val result = MutableLiveData<List<allskuOrderd>>()
        repository.skuTotalOrder(orderInfoId)
            .subscribe({
                result.postValue(it.body()!!.skuorder)
            },{
                result.postValue(emptyList())
            }).isDisposed
        return result
    }
}