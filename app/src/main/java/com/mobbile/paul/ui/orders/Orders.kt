package com.mobbile.paul.ui.orders

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.startGoogleMapIntent
import kotlinx.android.synthetic.main.activity_all__orders.*
import kotlinx.android.synthetic.main.activity_all__orders.backbtn
import kotlinx.android.synthetic.main.activity_all__orders.orderbadgecounter
import kotlinx.android.synthetic.main.activity_modules.*
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.customersorders.view.*
import javax.inject.Inject


class Orders : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: OrderViewModel

    private lateinit var nAdapter: OrdersAdapter

    private var preferences: SharedPreferences? = null

    var employId:Int = 0

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all__orders)
        database = FirebaseDatabase.getInstance()
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        vmodel = ViewModelProviders.of(this, modelFactory)[OrderViewModel::class.java]
        employId = preferences!!.getInt("preferencesEmployeeID",0)
        initView()
    }

    private fun initView() {

        orderbadgecounter.visibility = View.INVISIBLE

        countBargeData()

        backbtn.setOnClickListener {
            onBackPressed()
        }

        vmodel.getcustomerOrder(employId).observe(this,customerOrders)
    }


    private fun countBargeData() {

        val references = database.getReference("/message/customer/$employId")
        references.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    orderbadgecounter.visibility = View.VISIBLE
                    orderbadgecounter.text = p0.childrenCount.toString()
                }else{
                    orderbadgecounter.visibility = View.INVISIBLE
                }
            }
        })
    }


    val customerOrders = Observer<List<allCustomerProductOrder>> {
        showProgressBar(false)
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

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.googlelocation -> {
                    googleDirection(item)
                }
                R.id.deliver -> {
                    orderDialog(item)
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun googleDirection(item : allCustomerProductOrder) {
        val dmode = "l".single()
        val destination = "${item.latitude},${item.longitude}"
        startGoogleMapIntent(this, destination, dmode, 't')
    }

    private fun orderDialog(item : allCustomerProductOrder) {
        val orderIntents = Intent(this, OrderSummary::class.java)
        orderIntents.putExtra("orderId", item.orderid)
        orderIntents.putExtra("outletAddress", item.outletaddress)
        orderIntents.putExtra("contactPhone", item.contactphone)
        orderIntents.putExtra("outletName", item.outletname)
        orderIntents.putExtra("latitude", item.latitude)
        orderIntents.putExtra("longitude", item.longitude)
        orderIntents.putExtra("uid", item.uid)
        orderIntents.putExtra("token", item.token)
        startActivity(orderIntents)
    }

    companion object {
        val TAG = "Orders"
    }

}
