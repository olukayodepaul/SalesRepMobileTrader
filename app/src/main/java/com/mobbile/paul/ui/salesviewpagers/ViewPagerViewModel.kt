package com.mobbile.paul.ui.salesviewpagers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.ApplicationCustomers
import com.mobbile.paul.model.toEntityAllOutletsList
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ChatMessages
import com.mobbile.paul.util.Util.getDate
import javax.inject.Inject

class ViewPagerViewModel @Inject constructor(private var repo: Repository) : ViewModel() {


    private var res = MutableLiveData<String>()

    fun ReloadCustomers(): MutableLiveData<String> {
        return res
    }

    fun fetchsAllCustomers(): LiveData<String> {
        val responses = MutableLiveData<String>()
        repo.getCustomersEntryCount()
            .subscribe({
               responses.postValue("SUCCESS~$it")
            }, {
                responses.postValue("FAIL~500")
            })
            .isDisposed
        return responses
    }

    fun getAllCustomerFromRemoteServer(employee_id: Int) {
        repo.getAllCustomers(employee_id)
            .subscribe({
                if(it.isSuccessful && it.body() != null && it.code() == 200 && it.body()!!.status == 200 && it.body()!!.alloutlets.isNotEmpty() && it.body()!!.alloutlets.size > 3){
                    saveAllCustomerToLocalDB(it.body()!!.alloutlets)
                }else{
                    //manage here all there will be crashes on the application
                    res.postValue("FAIL")
                }
            }, {
            }).isDisposed
    }

    private fun saveAllCustomerToLocalDB(list: List<ApplicationCustomers>) {
        repo.saveAllCustomerToLocalDB(list.map { it.toEntityAllOutletsList() })
            .subscribe({
                res.postValue("SUCCESS")
            }, {
                res.postValue("FAIL")
            }).isDisposed
    }

    //this is the message
    fun saveRemotemossageOnLocalDb(msg: List<ChatMessages>) {
        msg.forEach {
            repo.ChatMessage(it.key,1, it.msg, it.timeago, it.dates)
                .subscribe({
                },{
                }).isDisposed
        }
    }

    //counting message
    fun countUnReadMessage(): LiveData<Int> {
        val nts = MutableLiveData<Int>()
        repo.countUnReadMessage().subscribe(
            {
                nts.postValue(it)
            },{
                nts.postValue(0)
            }
        ).isDisposed
        return nts
    }



}