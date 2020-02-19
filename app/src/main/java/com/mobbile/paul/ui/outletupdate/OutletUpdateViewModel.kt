package com.mobbile.paul.ui.outletupdate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.OutletUpdateParser
import com.mobbile.paul.model.spinersEntity
import com.mobbile.paul.provider.Repository
import javax.inject.Inject

class OutletUpdateViewModel @Inject constructor(private val repository: Repository): ViewModel(){


    fun fetchSpinners(): LiveData<List<spinersEntity>> {
        var mResult = MutableLiveData<List<spinersEntity>>()
        repository.fetchSpinners()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed

        return mResult
    }

    fun updateOutlet(tmid: Int, urno: Int, latitude: Double, longitude: Double, outletname: String, contactname: String,
                     outletaddress: String, contactphone: String, outletclassid: Int, outletlanguage: Int,
                     outlettypeid: Int): LiveData<OutletUpdateParser>{

        val mResult = MutableLiveData<OutletUpdateParser>()
        val rt = OutletUpdateParser()

        repository.updateOutlet(tmid,urno,latitude,longitude,outletname,contactname,outletaddress,contactphone,
            outletclassid,outletlanguage,outlettypeid)
            .subscribe({

                rt.notis = it.body()!!.notis
                rt.status = it.body()!!.status
                mResult.postValue(rt)

            },{
                rt.notis = it.message.toString()
                rt.status = 300
                mResult.postValue(rt)

            }).isDisposed
        return mResult
    }

}

