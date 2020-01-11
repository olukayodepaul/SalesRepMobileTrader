package com.mobbile.paul.ui.entryhistory


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.EntityGetSalesEntry
import com.mobbile.paul.model.SumSales
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_entry_history.*
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class EntryHistory : BaseActivity() {

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

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: EntryHistoryViewModel

    private lateinit var mAdapter: EntryHistoryAdapter

    private var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_history)
        vmodel = ViewModelProviders.of(this, modelFactory)[EntryHistoryViewModel::class.java]
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        initViews()
    }

    private fun initViews() {
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
        tv_outlet_name.text = outletname

        back_btn.setOnClickListener {
            onBackPressed()
        }

        btn_complete.setOnClickListener {

            when {
                token.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    btn_complete.visibility = View.INVISIBLE
                    vmodel.postSalesToServer(repid, currentlat, currentlng, outletlat, outletlng, distance,  durations, urno, visit_sequence,  auto, token_form.text.toString().trim())
                }
                defaulttoken.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    btn_complete.visibility = View.INVISIBLE
                    vmodel.postSalesToServer(repid, currentlat, currentlng, outletlat, outletlng, distance,  durations, urno, visit_sequence,  auto, token_form.text.toString().trim())
                }
                else -> showMessageDialogWithoutIntent(this,  "Error", "Invalid Customer Verification code")
            }

        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view_complete!!.setHasFixedSize(true)
        recycler_view_complete.layoutManager = layoutManager

        vmodel.fecthAllSalesEntries().observe(this, observerOfSalesEntry)
    }

    private val observerOfSalesEntry = Observer<List<EntityGetSalesEntry>> {
        if (it != null) {
            val list: List<EntityGetSalesEntry> = it
            mAdapter = EntryHistoryAdapter(list)
            mAdapter.notifyDataSetChanged()
            recycler_view_complete.adapter = mAdapter
            recycler_view_complete.setItemViewCacheSize(list.size)
            loadTotalSales()
        }
        showProgressBar(false)
    }

    private fun loadTotalSales() {
        vmodel.SumAllTotalSalesEntry().observe(this, obserTotal)
    }

    private val obserTotal = Observer<SumSales> {
        if (it != null) {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.FLOOR
            s_s_pricing.text = String.format("%,.1f",(df.format(it.spricing).toDouble()))
            s_s_invetory.text = String.format("%,.1f",(df.format(it.sinventory).toDouble()))
            s_s_order.text = String.format("%,.1f",(df.format(it.sorder).toDouble()))
            s_s_amount.text = String.format("%,.1f",(df.format(it.samount).toDouble()))
        }
    }



}
