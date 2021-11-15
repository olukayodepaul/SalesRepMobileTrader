package com.mobbile.paul.ui.entryhistory


import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.synthetic.main.activity_entry_history.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import java.text.DecimalFormat

class EntryHistory : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: EntryHistoryViewModel

    private lateinit var adapter: EntryHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_history)
        vmodel = ViewModelProviders.of(this, modelFactory)[EntryHistoryViewModel::class.java]
        initAdapter()
        vmodel.fetchAllSalesEntries()
        fetchAllSalesEntries()
    }

    private fun fetchAllSalesEntries() {
        lifecycleScope.launchWhenResumed {
            vmodel.entriesUiState.collect {
                it.let {
                    when(it){
                        is PostEntryUiState.Empty->{ }

                        is PostEntryUiState.Error->{
                            btn_complete_forground.isVisible = false
                        }

                        is PostEntryUiState.Loading->{

                        }

                        is PostEntryUiState.Success->{

                            if(it.data.isNotEmpty()) {

                                val isTotalAmount = it.data.sumByDouble{
                                        amount->amount.amount
                                }

                                val isTotalInventory = it.data.sumByDouble {
                                    inventory->inventory.inventory.toDouble()
                                }

                                val isTotalPricing = it.data.sumByDouble {
                                    pricing->pricing.pricing.toDouble()
                                }

                                val isTotalOrder = it.data.sumByDouble {
                                        orders->orders.orders.toDouble()
                                }


                                adapter = EntryHistoryAdapter(it.data)
                                adapter.notifyDataSetChanged()
                                recycler_view_complete.setItemViewCacheSize(it.data.size)
                                recycler_view_complete.adapter = adapter

                                val df = DecimalFormat("#.#")
                                s_s_pricing.text = String.format("%,.1f", (df.format(isTotalPricing)))
                                s_s_invetory.text = String.format("%,.1f", (df.format(isTotalInventory)))
                                s_s_order.text = String.format("%,.1f", (df.format(isTotalOrder)))
                                s_s_amount.text = String.format("%,.1f", (df.format(isTotalAmount)))
                                return@collect
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        recycler_view_complete.layoutManager = layoutManager
        recycler_view_complete.setHasFixedSize(true)
    }

//study the generic class
//public inline fun <T> Iterable<T>.sumByDouble(selector: (T) -> Double): Double {
//    var sum: Double = 0.0
//    for (element in this) {
//        sum += selector(element)
//    }
//    return sum
//}





}