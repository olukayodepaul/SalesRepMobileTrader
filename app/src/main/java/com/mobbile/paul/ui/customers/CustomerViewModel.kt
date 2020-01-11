package com.mobbile.paul.ui.customers


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.*
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.AttendanExchange
import com.mobbile.paul.util.ParserModel.ExchangeParser
import com.mobbile.paul.util.ParserModel.outletSequenceParser
import com.mobbile.paul.util.Util.getTime
import javax.inject.Inject

class CustomerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {

    private var response = MutableLiveData<CustomerExchage>()

    private var atresponse = MutableLiveData<AttendantParser>()

    fun CustomersObserver(): LiveData<CustomerExchage> {
        return response
    }

    fun CloseOutletObserver(): LiveData<AttendantParser> {
        return atresponse
    }

    private lateinit var outletClose: Attendant

    fun fetchsAllCustomers(employee_id: Int) {
        repo.getCustomersEntryCount()
            .subscribe({ count ->
                if (count == 0) {
                    getAllCustomerFromRemoteServer(employee_id)
                } else {
                    pullCustomersFromCustomerLocalDB()
                }
            }, {
                ExchangeParser(response, emptyList(), 400, it.message.toString())
            })
            .isDisposed
    }

    private fun getAllCustomerFromRemoteServer(employee_id: Int) {
        repo.getAllCustomers(employee_id)
            .subscribe({
                if(it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status == 200 && it.body()!!.alloutlets.isNotEmpty() && it.body()!!.alloutlets.size > 3){
                    saveAllCustomerToLocalDB(it.body()!!.alloutlets)
                }else{
                    ExchangeParser(response, emptyList(), 400, "Note that the problem could be from internet or no customers assign to you")
                }
            }, {
                ExchangeParser(response, emptyList(), 400, it.message.toString())
            }).isDisposed
    }

    private fun saveAllCustomerToLocalDB(list: List<ApplicationCustomers>) {
        repo.saveAllCustomerToLocalDB(list.map { it.toEntityAllOutletsList() })
            .subscribe({
                pullCustomersFromCustomerLocalDB()
            }, {
                ExchangeParser(response, emptyList(), 400, it.message.toString())
            }).isDisposed
    }

    private fun pullCustomersFromCustomerLocalDB() {
        repo.pullFromCustomerLocalDB().subscribe(
            {
                ExchangeParser(response, it, 200, "")
            },{
                ExchangeParser(response, emptyList(), 400, it.message.toString())
            }
        ).isDisposed
    }

    fun validateOutletSequence(id: Int, nexts: Int, currentLat:Double,currentLng:Double): LiveData<SequenceExchage> {
        Log.d(TAG, "Customers 10")
        val res = MutableLiveData<SequenceExchage>()
        repo.validateOutletSequence(id)
            .subscribe(
                {
                    val intArray = nexts in it.self.split(",").map { it.toInt() }

                    when {
                        nexts == it.nexts -> {
                            outletSequenceParser(res,200,currentLat,currentLng)
                        }
                        intArray -> {
                            outletSequenceParser(res,200,currentLat,currentLng)
                        }
                        else -> {
                            outletSequenceParser(res,300,currentLat,currentLng)
                        }
                    }
                }, {
                    outletSequenceParser(res,200,currentLat,currentLng) //change status t0 400
                }).isDisposed
        return res
    }

    //from here close outlet works
    fun closeOutlet(rep_id:Int, currentLat:String, currentLng:String, outletLat:String, outletLng:String, distance:String,
                    duration:String, urno:Int, sequenceno:Int, auto:Int) {
        repo.CloseOutlets(rep_id,  currentLat, currentLng, outletLat, outletLng, getTime(), sequenceno, distance, duration, urno)
            .subscribe({
                outletClose = it.body()!!
                UpdateSeque(1, sequenceno,auto)
            },{
                AttendanExchange(atresponse, 400, it.message.toString())
            }).isDisposed
    }

    private fun UpdateSeque(id: Int, sequenceno: Int, auto: Int) {
        repo.UpdateSeque(id,sequenceno+1, ",$sequenceno").subscribe({
            setEntryTime(auto)
        }, {
            AttendanExchange(atresponse, 400, it.message.toString())
        }).isDisposed
    }

    private fun setEntryTime(auto: Int) {
        repo.setEntryTime(getTime(), auto).subscribe({
            AttendanExchange(atresponse, outletClose.status, outletClose.notis)
        }, {
            AttendanExchange(atresponse, 400, it.message.toString())
        }).isDisposed
    }

    companion object{
        val TAG = "CustomerViewModel"
    }
}



