package com.mobbile.paul.ui.orders

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.synthetic.main.activity_all__orders.*
import kotlinx.android.synthetic.main.customersorders.view.*
import javax.inject.Inject


class Orders : BaseActivity() {

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

    private fun initView() {
        backbtn.setOnClickListener {
            onBackPressed()
        }

        vmodel.getcustomerOrder(2213).observe(this,customerOrders)
    }

    val customerOrders = Observer<List<allCustomerProductOrder>> {
        //please check if data is empty
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        _r_view_pager_order.layoutManager = layoutManager
        nAdapter = OrdersAdapter(it, this, ::modulesAdapterItems)
        nAdapter.notifyDataSetChanged()
        _r_view_pager_order.adapter = nAdapter
    }

    private fun modulesAdapterItems( item : allCustomerProductOrder,widget: View) {

        val popupMenu = PopupMenu(this, widget.menu_icon)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.floatingordermenu, popupMenu.menu)

        popupMenu.show()

    }

    companion object{
        val TAG = "Orders"
    }
}
