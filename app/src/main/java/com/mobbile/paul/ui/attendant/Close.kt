package com.mobbile.paul.ui.attendant


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.AttendantParser
import com.mobbile.paul.model.mSalesDetailsParser
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.getDate
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_close.*
import javax.inject.Inject

class Close : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: AttendantViewModel

    private lateinit var nAdapter: CloseAdapter

    var mode: Int = 0
    var repid: Int = 0
    var outletlat: Double = 0.0
    var outletlng: Double = 0.0
    var sequenceno: Int = 0
    var distance: String = ""
    var duration: String = ""
    var customer_code: String = ""
    var sortid: Int = 0
    var auto: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close)
        vmodel = ViewModelProviders.of(this, modelFactory)[AttendantViewModel::class.java]
        initViews()
    }

    private fun initViews() {

        repid = intent.getIntExtra("repid", 0)
        outletlat = intent.getDoubleExtra("outletlat", 0.0)
        outletlng = intent.getDoubleExtra("outletlng", 0.0)
        sequenceno = intent.getIntExtra("sequenceno", 0)
        distance = intent.getStringExtra("distance")!!
        duration = intent.getStringExtra("duration")!!
        customer_code = intent.getStringExtra("customer_code")!!
        sortid = intent.getIntExtra("sort", 0)
        auto = intent.getIntExtra("auto", 0)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
        _r_view_pager!!.setHasFixedSize(true)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        vmodel.getSalesDetails(customer_code).observe(this, observebasket)

        resumebtn.setOnClickListener { //clockin
            showProgressBar(true)
            vmodel.takeAttendantForOther(
                3, repid, outletlat, outletlng, 0.0, 0.0,
                distance, duration, "${sequenceno}", getDate(),auto
            )
        }

        clockoutbtn.setOnClickListener { //close
            showProgressBar(true)
            vmodel.takeAttendantForOther(
                4, repid, outletlat, outletlng, 0.0, 0.0,
                distance, duration, "${sequenceno}", getDate(),auto
            )
        }

        vmodel.attendantExchange().observe(this, observeAttendant)
    }

    private val observeAttendant = Observer<AttendantParser> {
        when (it.status) {
            200 -> {
                showProgressBar(false)
                if (mode == 1) {
                    showMessageDialogWithoutIntent(this, "Successful", it.notis)
                } else {
                    showMessageDialogWithIntent(SalesViewPager(), this, "Successful", it.notis)
                }
            }
            else -> {
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "Error", it.notis)
            }
        }
    }

    private val observebasket = Observer<mSalesDetailsParser> {
        val data: mSalesDetailsParser = it
        when (it.status) {
            200 -> {
                showProgressBar(false)
                nAdapter = CloseAdapter(data.list!!)
                nAdapter.notifyDataSetChanged()
                _r_view_pager.setItemViewCacheSize(data.list!!.size)
                _r_view_pager.adapter = nAdapter
            }
            else -> {
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "Error", "${it.msg} ${customer_code}")
            }
        }
    }


}
