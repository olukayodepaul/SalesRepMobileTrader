package com.mobbile.paul.ui.entries


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.SalesEntryExchage
import com.mobbile.paul.model.setSalesEntry
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.entryhistory.EntryHistory
import com.mobbile.paul.util.Util.getTime
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_entries.*
import kotlinx.android.synthetic.main.sales_entry_adapter.view.*
import javax.inject.Inject

class Entries : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: EntriesViewModel

    private lateinit var mAdapter: EntriesAdapter

    private var preferences: SharedPreferences? = null

    var repid: Int = 0
    var currentlat: String = "0.0"
    var currentlng: String = "0.0"
    var outletlat: String = "0.0"
    var outletlng: String = "0.0"
    var distance: String = "0 km"
    var durations: String = "0 MS"
    var urno: Int = 0
    var visit_sequence: Int = 0
    var token: String = ""
    var outletname: String = ""
    var defaulttoken: String = ""
    var customerno: String = ""
    var customer_code: String = ""
    var auto: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entries)
        initViews()

        back_btn.setOnClickListener {
            onBackPressed()
        }

        save_sales_entry.setOnClickListener {
            validateSalesEntries()
        }
    }

    private fun initViews(){

        repid = intent.getIntExtra("repid", 0)
        currentlat = intent.getStringExtra("currentlat")!!
        currentlng = intent.getStringExtra("currentlng")!!
        outletlat = intent.getStringExtra("outletlat")!!
        outletlng = intent.getStringExtra("outletlng")!!
        distance = intent.getStringExtra("distance")!!
        durations = intent.getStringExtra("durations")!!
        urno = intent.getIntExtra("urno", 0)
        visit_sequence = intent.getIntExtra("visit_sequence", 0)
        token = intent.getStringExtra("token")!!
        outletname = intent.getStringExtra("outletname")!!
        defaulttoken = intent.getStringExtra("defaulttoken")!!
        customerno = intent.getStringExtra("customerno")!!
        customer_code = intent.getStringExtra("customer_code")!!
        auto = intent.getIntExtra("auto", 0)

        vmodel = ViewModelProviders.of(this, modelFactory)[EntriesViewModel::class.java]
        vmodel.fetchSales(customerno, customer_code, repid)
        vmodel.getSalesEntryExchage().observe(this,observeSalesEntry)
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        tv_outlet_name.text = outletname

        initAdapter()
    }

    private fun initAdapter() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _sales_entry_recycler.layoutManager = layoutManager
        _sales_entry_recycler.setHasFixedSize(true)
    }

    private val observeSalesEntry = Observer<SalesEntryExchage> {
        when(it.status) {
            "200"-> {
                showProgressBar(false)
                val list: List<setSalesEntry> = it.data!!
                mAdapter = EntriesAdapter(list, ::adapterItemClicked)
                mAdapter.notifyDataSetChanged()
                _sales_entry_recycler.setItemViewCacheSize(list.size)
                _sales_entry_recycler.adapter = mAdapter
            }else-> {
                showMessageDialogWithoutIntent(this, "Error","")
            }
        }
    }

    private fun adapterItemClicked(partItem: setSalesEntry, containerView: View) {

        var trasformPricing = 0
        var trasformInventory = 0.0
        var trasformOrder = 0.0
        var controltrasformPricing = ""
        var controltrasformInventory = ""
        var controltrasformOrder = ""

        if (containerView.mt_pricing.text.toString().isNotEmpty()) {
            trasformPricing = containerView.mt_pricing.text.toString().toInt()
            controltrasformPricing = "0"
        }

        if(containerView.mt_inventory.text.toString()==".") {
            containerView.mt_inventory.setText("")
            controltrasformInventory = ""
        }else if (containerView.mt_inventory.text.toString().isNotEmpty()) {
            trasformInventory = containerView.mt_inventory.text.toString().toDouble()
            controltrasformInventory = "0"
        }

        if(containerView.mt_order.text.toString()==".") {
            containerView.mt_order.setText("")
            controltrasformOrder = ""
        }else if (containerView.mt_order.text.toString().isNotEmpty()) {
            trasformOrder = containerView.mt_order.text.toString().toDouble()
            if(trasformOrder > partItem.order_sold){
                controltrasformOrder = ""
                containerView.mt_order.setText("")
            }else{
                controltrasformOrder = "0"
            }
        }
        vmodel.updateDailySales(trasformInventory, trasformPricing, trasformOrder, getTime(), controltrasformPricing, controltrasformInventory,controltrasformOrder, partItem.product_code)
    }

    private fun validateSalesEntries(){
        vmodel.validateEntryStatus().observe(this, observeValidateSalesEntries)
    }

    private val observeValidateSalesEntries = Observer<Int>{

        if(it==0) {
            showProgressBar(false)
            val intent = Intent(this, EntryHistory::class.java)
            intent.putExtra("repid", repid)
            intent.putExtra("currentlat",currentlat)
            intent.putExtra("currentlng", currentlng)
            intent.putExtra("outletlat", outletlat)
            intent.putExtra("outletlng", outletlng)
            intent.putExtra("distance",distance)
            intent.putExtra("durations",durations)
            intent.putExtra("urno", urno)
            intent.putExtra("visit_sequence", visit_sequence)
            intent.putExtra("token", token)
            intent.putExtra("outletname",outletname)
            intent.putExtra("defaulttoken", defaulttoken)
            intent.putExtra("auto", auto)
            intent.putExtra("customerno", customerno)
            intent.putExtra("customer_code", customer_code)
            startActivity(intent)
        }else {
            showProgressBar(false)
            showMessageDialogWithoutIntent(this, "Error entries", "Please enter all the fields and save. Thanks!")
        }
    }

    companion object{
        var TAG = "Entries"
    }

}

