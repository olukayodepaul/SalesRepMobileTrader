package com.mobbile.paul.ui.attendant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.Attendant
import com.mobbile.paul.model.AttendantParser
import com.mobbile.paul.model.Basket
import com.mobbile.paul.model.ProductParser
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.AttendanExchange
import com.mobbile.paul.util.ParserModel.repBaskets
import com.mobbile.paul.util.Util.getTime
import javax.inject.Inject

class AttendantViewModel @Inject constructor(private var repository: Repository): ViewModel() {


    lateinit var  attadant: Attendant

    private val mutable = MutableLiveData<AttendantParser>()

    fun attendantExchange(): LiveData<AttendantParser> {
        return mutable
    }

    fun getRepBasket(customerno: String): LiveData<ProductParser> {
        val mResult = MutableLiveData<ProductParser>()
        repository.getCustomerNo(customerno)
            .subscribe({
                val data: Basket = it.body()!!
                if (data.status == 200) {
                    repBaskets(mResult, data.status, data.notis, data.allrepsproducts!!)
                } else {
                    repBaskets(mResult, data.status, data.notis, emptyList())
                }
            }, {
                repBaskets(mResult, 400, "No Basket available for this Rep", emptyList())
            }).isDisposed
        return mResult
    }

    fun takeAttendant(taskid: Int, repid: Int, outletlat:Double, outletlng:Double, currentLat:Double,
                      currentLng:Double, distance:String, duration:String, sequenceno:String, arrivaltime:String) {
        repository.takeAttendant(taskid, repid, outletlat, outletlng, currentLat, currentLng, distance, duration, sequenceno, arrivaltime)
            .subscribe({
                attadant = it.body()!!
                if (attadant.status == 200 && taskid == 2) {
                    setAttendantTime(getTime(), sequenceno)
                } else {
                    AttendanExchange(mutable, attadant.status, attadant.notis)
                }
            }, {
                AttendanExchange(mutable, 400, "${it.message}")
            }).isDisposed
    }

    private fun setAttendantTime(time:String,sequenceno:String) {
        repository.setAttendantTime(time)
            .subscribe({
                SequencetManager(sequenceno)
            },{
                AttendanExchange(mutable, 400, "${it.message}")
            }).isDisposed
    }

    private fun SequencetManager(sequenceno:String) {
        repository.SequencetManager(1, sequenceno.toInt()+1, "0,${sequenceno}")
            .subscribe({
                AttendanExchange(mutable, attadant.status, attadant.notis)
            },{
                AttendanExchange(mutable, 400, "${it.message}")
            }).isDisposed
    }
}