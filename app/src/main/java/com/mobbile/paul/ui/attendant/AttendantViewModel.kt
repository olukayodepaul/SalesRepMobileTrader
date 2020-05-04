package com.mobbile.paul.ui.attendant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.*
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.AttendanExchange
import com.mobbile.paul.util.ParserModel.mBankDetails
import com.mobbile.paul.util.ParserModel.mSalesDetails
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

    fun takeAttendantForOther(taskid: Int, repid: Int, outletlat:Double, outletlng:Double, currentLat:Double,
                      currentLng:Double, distance:String, duration:String, sequenceno:String, arrivaltime:String, auto:Int) {
        repository.takeAttendant(taskid, repid, outletlat, outletlng, currentLat, currentLng, distance, duration, sequenceno, arrivaltime)
            .subscribe({
                attadant = it.body()!!
                if (attadant.status == 200) {
                    setEntryTime(getTime(), auto)
                } else {
                    AttendanExchange(mutable, attadant.status, attadant.notis)
                }
            }, {
                AttendanExchange(mutable, 400, "${it.message}")
            }).isDisposed
    }

    private fun setEntryTime(time:String,auto:Int) {
        repository.setEntryTime(time,auto)
            .subscribe({
                AttendanExchange(mutable, attadant.status, attadant.notis)
            },{
                AttendanExchange(mutable, 400, "${it.message}")
            }).isDisposed
    }

    private fun SequencetManager(sequenceno:String) {
        repository.SequencetManager(1, sequenceno.toInt()+1, "99999999,${sequenceno}") // for the sales rep only
        //repository.SequencetManager(1, sequenceno.toInt()+1, "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50")
            .subscribe({
                AttendanExchange(mutable, attadant.status, attadant.notis)
            },{
                AttendanExchange(mutable, 400, "${it.message}")
            }).isDisposed
    }

    fun getSalesDetails(customer_code: String): LiveData<mSalesDetailsParser> {
        val mResult = MutableLiveData<mSalesDetailsParser>()
        repository.getSalesDetails(customer_code)
            .subscribe({
                val data: SalesDetails = it.body()!!
                if (data.status == 200) {
                    mSalesDetails(mResult,data.status, data.notis, data.details)
                } else {
                    mSalesDetails(mResult,data.status, data.notis, emptyList())
                }
            }, {
                mSalesDetails(mResult,400,it.message.toString(), emptyList())
            }).isDisposed
        return mResult
    }

    fun getBankDetails(customer_code: String): LiveData<mBankDetailsParser> {
        val mResult = MutableLiveData<mBankDetailsParser>()
        repository.getBankDetails(customer_code)
            .subscribe({
                    mBankDetails(mResult,200,"",it.body()!!)
            }, {
                mBankDetails(mResult,400,it.message.toString(),BankDetails())
            }).isDisposed
        return mResult
    }


}