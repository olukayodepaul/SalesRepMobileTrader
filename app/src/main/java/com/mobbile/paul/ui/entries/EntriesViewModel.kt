package com.mobbile.paul.ui.entries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.InitBasket
import com.mobbile.paul.model.SalesEntryExchage
import com.mobbile.paul.model.toEntityGetSalesEntry
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.salesEntriesParsers
import javax.inject.Inject

class EntriesViewModel @Inject constructor(private var repo: Repository) :ViewModel() {

    private var salesEntry = MutableLiveData<SalesEntryExchage>()

    fun getSalesEntryExchage(): MutableLiveData<SalesEntryExchage> {
        return salesEntry
    }

    private var dataValues: InitBasket? = null

    fun fetchSales(customerno: String, customer_code: String, repid: Int) {
        repo.getBasket(customerno, customer_code, repid)
            .subscribe(
                {
                    dataValues = it.body()!!
                    deleteEntityAndGetSalesEntry()
                }
            ) {

            }.isDisposed
    }

    private fun deleteEntityAndGetSalesEntry() {
        repo.deleteEntityGetSalesEntry().subscribe({
            createDailySales()
        }, {
            salesEntriesParsers(salesEntry, "400","${it.message}", emptyList())
        }).isDisposed
    }

    private fun createDailySales() {
        repo.createDailySales(dataValues!!.entry!!.map { it.toEntityGetSalesEntry() })
            .subscribe({
                salesEntriesParsers(salesEntry,dataValues!!.status ,"", dataValues!!.sales!!)
            }, {
                salesEntriesParsers(salesEntry, "400","${it.message}", emptyList())
            }).isDisposed
    }

    fun updateDailySales(inventory: Double, pricing: Int, order:Double, entry_time: String, controlpricing:String, controlinventory:String,controlorder:String, product_code:String){
        repo.updateDailySales(inventory,pricing,order,entry_time,controlpricing,controlinventory,controlorder,product_code)
            .subscribe({
            },{
            }).isDisposed
    }

    fun validateEntryStatus(): MutableLiveData<Int> {
        var mResult = MutableLiveData<Int>()
        repo.validateSalesEntry()
            .subscribe({
                mResult.postValue(it)
            }, {
                mResult.postValue(null)
            }).isDisposed
        return mResult
    }

    companion object{
        val TAG = "EntriesViewModel"
    }

}