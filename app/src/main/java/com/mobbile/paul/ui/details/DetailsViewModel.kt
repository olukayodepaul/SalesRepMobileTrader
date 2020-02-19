package com.mobbile.paul.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.DetailsForEachSales
import com.mobbile.paul.model.mDetailsForEachSalesParser
import com.mobbile.paul.provider.Repository
import com.mobbile.paul.util.ParserModel.mDetailsForEachSales
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private var repository: Repository) : ViewModel() {

    fun getDetailsForEachSales(rep_id:Int,urno:Int): LiveData<mDetailsForEachSalesParser> {
        val mResult = MutableLiveData<mDetailsForEachSalesParser>()
        repository.getDetailsForEachSales(rep_id,urno)
            .subscribe({
                mDetailsForEachSales(mResult,200,"",it.body()!!)
            }, {
                mDetailsForEachSales(mResult,400,it.message.toString(),DetailsForEachSales())
            }).isDisposed
        return mResult
    }

}