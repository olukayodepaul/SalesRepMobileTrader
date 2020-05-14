package com.mobbile.paul.ui.orders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.model.realOrder
import com.mobbile.paul.model.skuOrderd
import com.mobbile.paul.provider.Repository
import javax.inject.Inject

class OrderViewModel @Inject constructor(private var repository: Repository): ViewModel() {

    fun getcustomerOrder(employeeid:Int) : LiveData<List<allCustomerProductOrder>> {
        val result = MutableLiveData<List<allCustomerProductOrder>>()
        repository.customerOrder(employeeid)
            .subscribe({
                result.postValue(it.body()!!.order)
            },{
                Log.d(TAG, it.message.toString())
                result.postValue(emptyList())
            }).isDisposed
        return result
    }

    fun getskuTotalOrder(orderInfoId:Int) : LiveData<skuOrderd> {
        val result = MutableLiveData<skuOrderd>()
        repository.skuTotalOrder(orderInfoId)
            .subscribe({
                result.postValue(it.body())
            },{
            }).isDisposed
        return result
    }

    fun getskuOrderProducts(employeeid: Int, orderid: Int) : LiveData<realOrder> {
        val result = MutableLiveData<realOrder>()
        repository.orderProducts(employeeid, orderid)
            .subscribe({
                result.postValue(it.body())
            },{
                val buildResponse = realOrder()
                buildResponse.msg = it.message.toString()
                buildResponse.status = 500
                result.postValue(buildResponse)
            }).isDisposed
        return result
    }

    companion object{
        val TAG = "OrderViewModel"
    }
}