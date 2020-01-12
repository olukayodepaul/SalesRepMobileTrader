package com.mobbile.paul.ui.details

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.mDetailsForEachSalesParser
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class Details : BaseActivity() {

    var urno: Int = 0
    var rep_id: Int = 0
    var outletname: String = ""

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: DetailsViewModel

    private lateinit var mAdapter: DetailsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        vmodel = ViewModelProviders.of(this, modelFactory)[DetailsViewModel::class.java]
        initViews()
    }

    private fun initViews() {

        urno = intent.getIntExtra("urno", 0)
        rep_id = intent.getIntExtra("rep_id", 0)
        outletname = intent.getStringExtra("outletname")!!
        tv_outlet_name.text = outletname

        backbtn.setOnClickListener {
            onBackPressed()
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager.layoutManager = layoutManager
        _r_view_pager!!.setHasFixedSize(true)

        vmodel.getDetailsForEachSales(rep_id,urno).observe(this, Observer <mDetailsForEachSalesParser>{
            when (it.status) {
                200 -> {
                    showProgressBar(false)
                    mAdapter = DetailsAdapter(it.list!!.details)
                    mAdapter.notifyDataSetChanged()
                    _r_view_pager.setItemViewCacheSize(it.list!!.details.size)
                    _r_view_pager.adapter = mAdapter
                }
                else -> {
                    showProgressBar(false)
                    showMessageDialogWithoutIntent(this, "Error","${it.msg}")
                }
            }
        })
    }
}
