package com.mobbile.paul.ui.entryhistory


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.AttendantParser
import com.mobbile.paul.model.EntityGetSalesEntry
import com.mobbile.paul.model.SumSales
import com.mobbile.paul.model.customersEntity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_entry_history.*
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class EntryHistory : BaseActivity() {

    var currentlat: String = "0.0"
    var currentlng: String = "0.0"
    var uiid: String = ""
    private lateinit var customers: customersEntity

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
        customers = intent.extras!!.getParcelable("extra_item")!!
        initViews()
    }

    private fun initViews() {

        btn_complete_forground.visibility = View.GONE

        currentlat = intent.getStringExtra("currentlat")!!
        currentlng = intent.getStringExtra("currentlng")!!
        uiid = intent.getStringExtra("uiid")!!
        tv_outlet_name.text = customers.outletname

        back_btn.setOnClickListener {
            onBackPressed()
        }

        btn_complete.setOnClickListener {

            btn_complete.visibility = View.GONE
            btn_complete_forground.visibility = View.VISIBLE

            when {
                customers.token.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    vmodel.postSalesToServer(
                        customers.rep_id,
                        currentlat,
                        currentlng,
                        customers.latitude.toString(),
                        customers.longitude.toString(),
                        customers.distance,
                        customers.duration,
                        customers.urno,
                        customers.sequenceno,
                        customers.auto,
                        token_form.text.toString().trim(),
                        uiid
                    )
                }
                customers.defaulttoken.equals(token_form.text.toString().trim()) -> {
                    showProgressBar(true)
                    vmodel.postSalesToServer(
                        customers.rep_id,
                        currentlat,
                        currentlng,
                        customers.latitude.toString(),
                        customers.longitude.toString(),
                        customers.distance,
                        customers.duration,
                        customers.urno,
                        customers.sequenceno,
                        customers.auto,
                        token_form.text.toString().trim(),
                        uiid
                    )
                }
                else -> {

                    btn_complete.visibility = View.INVISIBLE
                    btn_complete_forground.visibility = View.GONE

                    showMessageDialogWithoutIntent(
                        this,
                        "Error",
                        "Invalid Customer Verification code"
                    )
                }
            }

        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view_complete!!.setHasFixedSize(true)
        recycler_view_complete.layoutManager = layoutManager

        vmodel.fecthAllSalesEntries().observe(this, observerOfSalesEntry)
        vmodel.OpenOutletObserver().observe(this, observeCloseOutlets)
    }

    private val observeCloseOutlets = Observer<AttendantParser> {
        when (it.status) {
            200 -> {
                showProgressBar(false)
                customeSuccessDialog()
            }
            else -> {
                btn_complete.visibility = View.INVISIBLE
                btn_complete_forground.visibility = View.GONE
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "Outlet Close Error", it.notis)
            }
        }
    }

    private fun customeSuccessDialog() {
        val dialog = MaterialDialog(this)
            .cancelOnTouchOutside(false)
            .cancelable(false)
            .customView(R.layout.dialogs)
        dialog.findViewById<TextView>(R.id.positive_button).setOnClickListener {
            val intent = Intent(this, SalesViewPager::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
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
            s_s_pricing.text = String.format("%,.1f", (df.format(it.spricing).toDouble()))
            s_s_invetory.text = String.format("%,.1f", (df.format(it.sinventory).toDouble()))
            s_s_order.text = String.format("%,.1f", (df.format(it.sorder).toDouble()))
            s_s_amount.text = String.format("%,.1f", (df.format(it.samount).toDouble()))
        }
    }

}
