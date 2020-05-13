package com.mobbile.paul.ui.orders


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.allskuOrderd
import com.mobbile.paul.model.skuOrderd
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.BargeMessages
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import kotlinx.android.synthetic.main.activity_order_summary.*
import kotlinx.android.synthetic.main.dialogfortoken.*
import kotlinx.android.synthetic.main.tokentoolbar.*
import javax.inject.Inject


class OrderSummary : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: OrderViewModel

    private lateinit var nAdapter: OrderSummaryAdapter

    private var preferences: SharedPreferences? = null

    var employId:Int = 0

    private lateinit var database: FirebaseDatabase

    var outletAddress: String = ""
    var contactPhone: String = ""
    var outletName: String = ""
    var latitude: String = ""
    var longitude: String = ""
    var token: Int = 0
    var uidForOrderConfirmation:String= ""
    var orderId: Int = 0

    var REQUEST_PHONE_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        vmodel = ViewModelProviders.of(this, modelFactory)[OrderViewModel::class.java]


        database = FirebaseDatabase.getInstance()
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        employId = preferences!!.getInt("preferencesEmployeeID",0)

        outletAddress = intent.getStringExtra("outletAddress")!!
        contactPhone = intent.getStringExtra("contactPhone")!!
        outletName = intent.getStringExtra("outletName")!!
        latitude = intent.getStringExtra("latitude")!!
        longitude = intent.getStringExtra("longitude")!!
        orderId = intent.getIntExtra("orderId",0)
        uidForOrderConfirmation = intent.getStringExtra("uid")!!
        token = intent.getIntExtra("token",0)

        custName.text = outletName
        address.text = outletAddress
        contactphone.text = contactPhone
        order_id.text = orderId.toString()
        initView()
    }

    private fun initView() {

        orderbadgecounter.visibility = View.INVISIBLE
        countBargeData()
        confirmOrder.setOnClickListener {
            confirmTokenDialog()
        }

        contactphone.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions( this, arrayOf(Manifest.permission.CALL_PHONE),REQUEST_PHONE_CODE )

            }else{
                makePhoneCall()
            }
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        vmodel.getskuTotalOrder(orderId).observe(this,customerOrdersSummary)
    }



    private val customerOrdersSummary = Observer<skuOrderd> {
        showProgressBar(false)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycleOrderHistory.layoutManager = layoutManager
        nAdapter = OrderSummaryAdapter(it.skuorder!!)
        nAdapter.notifyDataSetChanged()
        recycleOrderHistory.adapter = nAdapter
        price_tvs.text = (it.totalamount*it.totalqty).toString()
        qty_tvs.text  = it.totalqty.toString()
    }

    @SuppressLint("MissingPermission")
    private fun makePhoneCall(){
        val intent = Intent(Intent.ACTION_CALL, Uri.fromParts("tel",contactPhone,null))
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==REQUEST_PHONE_CODE){
            makePhoneCall()
        }
    }


    private fun confirmTokenDialog() {

        val dialog = MaterialDialog(this)
            .cancelOnTouchOutside(false)
            .cancelable(false)
            .customView(R.layout.dialogfortoken)
       dialog.close_dialog.setOnClickListener {
           dialog.dismiss()
        }

        dialog.confirmButtons.setOnClickListener {
            val confirmToken =
                dialog.et_1.text.toString()+""+dialog.et_2.text.toString()+""+dialog.et_3.text.toString()+""+dialog.et_4.text.toString()+""+dialog.et_5.text.toString()+""+dialog.et_6.text.toString()
            if(confirmToken == token.toString()) {
                closeOrderFromFirebase(uidForOrderConfirmation)
                Toast.makeText(this,"correct here 1",Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(this,"correct here 2",Toast.LENGTH_LONG).show()
            }
        }

        dialog.et_1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(dialog.et_1.text.length==1)
                {
                    dialog.et_2.requestFocus()
                }else {
                    dialog.et_1.requestFocus()
                }
            }
        })

        dialog.et_2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(dialog.et_2.text.length==1)
                {
                    dialog.et_3.requestFocus()
                }else {
                    dialog.et_1.requestFocus()
                }
            }
        })

        dialog.et_3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(dialog.et_3.text.length==1)
                {
                    dialog.et_4.requestFocus()
                }else {
                    dialog.et_2.requestFocus()
                }
            }
        })

        dialog.et_4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(dialog.et_4.text.length==1)
                {
                    dialog.et_5.requestFocus()
                }else {
                    dialog.et_3.requestFocus()
                }
            }
        })

        dialog.et_5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(dialog.et_5.text.length==1)
                {
                    dialog.et_6.requestFocus()
                }else {
                    dialog.et_4.requestFocus()
                }
            }
        })

        dialog.et_6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(dialog.et_6.text.length==1)
                {
                    dialog.et_6.requestFocus()
                }else {
                    dialog.et_5.requestFocus()
                }
            }
        })
        dialog.show()
    }

    private fun closeOrderFromFirebase(node: String) {
        val UID = BargeMessages(node)
        val references = database.getReference("/message/customer/$employId")
        references.child(UID.uid).removeValue()
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
}
