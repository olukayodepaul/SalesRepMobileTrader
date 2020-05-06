package com.mobbile.paul.ui.orders

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.showProgressBar
import kotlinx.android.synthetic.main.activity_all__orders.*
import javax.inject.Inject


class Orders : BaseActivity(){

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: OrderViewModel

    private lateinit var nAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all__orders)
        vmodel = ViewModelProviders.of(this, modelFactory)[OrderViewModel::class.java]
        initView()
    }

    private fun initView(){
        vmodel.getcustomerOrder(5).observe(this,customerOrders)
    }

    val customerOrders = Observer<List<allCustomerProductOrder>>{
        if(it.isEmpty()){
            val s = 10
        }else{
            showProgressBar(false, base_progress_bar)
            nAdapter = OrdersAdapter(it, this,::modulesAdapterItems)
            nAdapter.notifyDataSetChanged()
            _r_view_pager.setItemViewCacheSize(it.size)
            _r_view_pager.adapter = nAdapter
        }
    }

    private fun modulesAdapterItems(item : allCustomerProductOrder) {

    }
}
