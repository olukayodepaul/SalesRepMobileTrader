package com.mobbile.paul.ui.attendant


import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.AttendantParser
import com.mobbile.paul.model.mBankDetailsParser
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.getDate
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_banks.*
import javax.inject.Inject

class Banks : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var vmodel: AttendantViewModel

    private lateinit var bankAdapter: BankAdapter

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
        setContentView(R.layout.activity_banks)
        vmodel = ViewModelProviders.of(this, modelFactory)[AttendantViewModel::class.java]
        InitViews()
    }

    private fun InitViews(){
        repid = intent.getIntExtra("repid", 0)
        outletlat = intent.getDoubleExtra("outletlat", 0.0)
        outletlng = intent.getDoubleExtra("outletlng", 0.0)
        sequenceno = intent.getIntExtra("sequenceno", 0)
        distance = intent.getStringExtra("distance")!!
        duration = intent.getStringExtra("duration")!!
        customer_code = intent.getStringExtra("customer_code")!!
        sortid = intent.getIntExtra("sort", 0)
        auto = intent.getIntExtra("auto", 0)

        backbtn.setOnClickListener {
            onBackPressed()
        }

        vmodel.attendantExchange().observe(this, observeAttendant)

        resumebtn.setOnClickListener { //clockin
            showProgressBar(true)
            vmodel.takeAttendantForOther(
                5, repid, outletlat, outletlng, 0.0, 0.0,
                distance, duration, "${sequenceno}", getDate(),auto
            )
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
        _r_view_pager!!.setHasFixedSize(true)

        vmodel.getBankDetails(customer_code).observe(this, Observer<mBankDetailsParser> {
            when (it.status) {
                200 -> {
                    showProgressBar(false)
                    bankAdapter = BankAdapter(it.list!!.details)
                    bankAdapter.notifyDataSetChanged()
                    _r_view_pager.setItemViewCacheSize(it.list!!.details.size)
                    _r_view_pager.adapter = bankAdapter
                    order_tv_t.text = "${it.list!!.sumorder}"
                    amt_tv_t.text = "${it.list!!.sumdeposit}"
                    tv_aty_t_t.text = "${it.list!!.sumcom}"
                }
                else -> {
                    showProgressBar(false)
                    showMessageDialogWithoutIntent(this, "Error","${it.msg} ${customer_code}")
                }
            }
        })

        agent.setOnClickListener {
            val intent = Intent(this, Agent::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("repid", repid)
            startActivity(intent)
        }
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
}
