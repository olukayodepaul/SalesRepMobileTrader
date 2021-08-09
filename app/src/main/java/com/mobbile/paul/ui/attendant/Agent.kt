package com.mobbile.paul.ui.attendant

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.GetAllAgentDetailByRoute
import com.mobbile.paul.model.agentRoute
import com.mobbile.paul.model.customersEntity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import com.mobbile.paul.util.Util.startGoogleMapIntent
import kotlinx.android.synthetic.main.activity_agent.*
import retrofit2.Response
import javax.inject.Inject


class Agent : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    private lateinit var vmodel: AttendantViewModel

    private lateinit var agentAdapter: AgentAdapter

    var repid:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent)
        repid = intent.getIntExtra("repid",0)
        vmodel = ViewModelProviders.of(this, modelFactory)[AttendantViewModel::class.java]
        agentDetailsRequest(repid)

        backbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun agentDetailsRequest(repids:Int) {
        vmodel.getAgentDetails(repids).observe(this, Observer<Response<agentRoute>> {
            showProgressBar(false)
            if(it.code()==200) {
                if(it.body()!!.status==200) {
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                    _r_agents.layoutManager = layoutManager
                    _r_agents!!.setHasFixedSize(true)
                    agentAdapter = AgentAdapter(it.body()!!.agent!!, this, ::partItemClicked)
                    agentAdapter.notifyDataSetChanged()
                    _r_agents.setItemViewCacheSize(it.body()!!.agent!!.size)
                    _r_agents.adapter = agentAdapter
                }else{
                    showMessageDialogWithoutIntent(this,"Error", it.body()!!.massage)
                }
            }else{
                showMessageDialogWithoutIntent(this,"Error","Please Try again")
            }
        })
    }

    private fun partItemClicked(partItem: GetAllAgentDetailByRoute,menuSelected:Int) {
        if(menuSelected==1){
            val dmode = "d".single()
            val destination = "${partItem.latitude},${partItem.longitude}"
            startGoogleMapIntent(this, destination, dmode, 't')
        }else {
            showMessageDialogWithoutIntent(this,"Outlet Details", "Outlet Name: ${partItem.outletname}. Outlet Address: ${partItem.outletaddress}. Mobile Number: ${partItem.contact}")
        }
    }
}