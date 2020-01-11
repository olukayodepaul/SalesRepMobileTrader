package com.mobbile.paul.ui.entryhistory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.*
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.Util.getTime
import javax.inject.Inject

class EntryHistoryViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    fun fecthAllSalesEntries(): LiveData<List<EntityGetSalesEntry>> {
        var mResult = MutableLiveData<List<EntityGetSalesEntry>>()
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

    private fun pullAllSalesEntry(
        repid: Int, currentlat: String, currentlng: String, outletlat: String, outletlng: String,
        distance: String, duration: String, urno: Int,
        visitsequence: Int, auto: Int, token: String, lists: List<getSalesEntry>
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
        list.lists = lists

        repository.fetchPostSales(list)
            .subscribe({

            }, {

            }).isDisposed
    }

    fun postSalesToServer(
        repid: Int, currentlat: String, currentlng: String, outletlat: String, outletlng: String,
        distance: String, duration: String, urno: Int, visitsequence: Int, auto: Int, token: String
    ) {
        repository.pullAllSalesEntry()
            .subscribe({data ->
            pullAllSalesEntry(
                repid, currentlat,currentlng, outletlat, outletlng,
                distance, duration, urno, visitsequence, auto, token,
                data.map {it.toAllOutletsList()}
            )
        },{

        }).isDisposed
    }

}