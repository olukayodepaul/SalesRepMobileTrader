package com.mobbile.paul.ui.entryhistory


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.*
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.AttendanExchange
import com.mobbile.paul.util.Util.getTime
import javax.inject.Inject

class EntryHistoryViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    private var atresponse = MutableLiveData<AttendantParser>()

    fun OpenOutletObserver(): LiveData<AttendantParser> {
        return atresponse
    }

    private lateinit var outletOpen: Attendant

    fun fecthAllSalesEntries(): LiveData<List<EntityGetSalesEntry>> {
        val mResult = MutableLiveData<List<EntityGetSalesEntry>>()
        repository.fetchAllEntryPerDaily()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed
        return mResult
    }

    fun SumAllTotalSalesEntry(): LiveData<SumSales> {
        val mResult = MutableLiveData<SumSales>()
        repository.SumAllTotalSalesEntry()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed
        return mResult
    }

    fun postSalesToServer(
        repid: Int, currentlat: String, currentlng: String, outletlat: String, outletlng: String,
        distance: String, duration: String, urno: Int, visitsequence: Int, auto: Int, token: String,uiid:String
    ) {
        repository.pullAllSalesEntry()
            .subscribe({data ->
                pullAllSalesEntry(
                    repid, currentlat,currentlng, outletlat, outletlng,
                    distance, duration, urno, visitsequence, auto, token,
                    data.map {it.toAllOutletsList()},uiid
                )
            },{

            }).isDisposed
    }

    private fun pullAllSalesEntry(
        repid: Int, currentlat: String, currentlng: String, outletlat: String, outletlng: String,
        distance: String, duration: String, urno: Int,
        visitsequence: Int, auto: Int, token: String, lists: List<getSalesEntry>, uiid:String
    ) {
        val list = postToServer()
        list.repid = repid
        list.currentlat = currentlat
        list.currentlng = currentlng
        list.outletlat = outletlat
        list.outletlng = outletlng
        list.arrivaltime = getTime()
        list.visitsequence = visitsequence
        list.distance = distance
        list.duration = duration
        list.urno = urno
        list.token = token
        list.uiid = uiid
        list.lists = lists

        repository.fetchPostSales(list)
            .subscribe({
                outletOpen= it.body()!!
                UpdateSeque(1, visitsequence,auto)
            },{
                AttendanExchange(atresponse, 400, it.message.toString())
            }).isDisposed
    }



    private fun UpdateSeque(id: Int, sequenceno: Int, auto: Int) {
        repository.UpdateSeque(id, sequenceno+1, ",$sequenceno").subscribe({
            setEntryTime(auto)
        }, {
            AttendanExchange(atresponse, 400, it.message.toString())
        }).isDisposed
    }

    private fun setEntryTime(auto: Int) {
        repository.setEntryTime(getTime(), auto).subscribe({
            AttendanExchange(atresponse, outletOpen.status, outletOpen.notis)
        }, {
            AttendanExchange(atresponse, 400, it.message.toString())
        }).isDisposed
    }

}