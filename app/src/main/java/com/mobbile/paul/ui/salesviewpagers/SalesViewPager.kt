package com.mobbile.paul.ui.salesviewpagers


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.comission.Comission
import com.mobbile.paul.ui.customers.Customers
import com.mobbile.paul.ui.modules.Modules
import com.mobbile.paul.util.ChatMessages
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import com.mobbile.paul.util.Util.showProgressBar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sales_view_pager.*
import javax.inject.Inject

class SalesViewPager : DaggerAppCompatActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: ViewPagerViewModel

    private var preferences: SharedPreferences? = null

    private lateinit var database: FirebaseDatabase

    private val latestMessagesMap = HashMap<String, ChatMessages>()

    private var regionId = 0

    private val bt = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.sales -> {
                val fragment = Customers()
                tv_modules.text = "Sales"
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_commission -> {
                val fragment = Comission()
                tv_modules.text = "Commission"
                replaceFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.view_pager, fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_view_pager)
        showProgressBar(false, base_progress_bar_s)
        vmodel = ViewModelProviders.of(this, modelFactory)[ViewPagerViewModel::class.java]
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)

        database = FirebaseDatabase.getInstance()

        settings_btn.visibility = View.INVISIBLE
        dadgecounter.visibility = View.INVISIBLE
        confirmNewMessages()
        listenForMessages()

        tv_outlet_name.text = preferences!!.getString("preferencesEmployeeName","")!!
        regionId = preferences!!.getInt("preferencesEmployeeRegionId",0)


        backbtn.setOnClickListener {
            onBackPressed()
        }

        vmodel.fetchsAllCustomers().observe(this, Observer<String> {
            val splits = it.split("~")
            if(splits[0] == "SUCCESS") {
                if(splits[1].toInt() <= 4) {
                    settings_btn.visibility = View.VISIBLE
                }else{
                    settings_btn.visibility = View.INVISIBLE
                }
            }else{
                settings_btn.visibility = View.INVISIBLE
            }
        })

        settings_btn.setOnClickListener {
            showProgressBar(true, base_progress_bar_s)
            vmodel.getAllCustomerFromRemoteServer(preferences!!.getInt("preferencesEmployeeID",0))
        }

        val fragment = Customers()
        tv_modules.text = "Sales"
        settings_btn.visibility = View.VISIBLE
        replaceFragment(fragment)
        navigations.setOnNavigationItemSelectedListener(bt)

        vmodel.ReloadCustomers().observe(this, Observer {
            if(it=="FAIL"){
                showMessageDialogWithoutIntent(this, "Error", "Can't fetch customer at this moment, try again")
            }else{
                showMessageDialogWithIntent(Modules(), this, "Successful", "Customer successfully synchronise. You will be redirect to the Modules")
            }
        })
    }

    //listening to new messages
    private fun listenForMessages() {

        val regionId = preferences!!.getInt("preferencesEmployeeRegionId",0)
        Log.d(TAG, "$regionId")
        val refences = database.getReference("/message/$regionId")
        refences.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage =  p0.getValue(ChatMessages::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    private fun refreshRecyclerViewMessages() {
        val list = ArrayList<ChatMessages>()
        latestMessagesMap.values.forEach {
            list.add(it)
        }
        vmodel.saveRemotemossageOnLocalDb(list)
        confirmNewMessages()
    }

    private fun confirmNewMessages() {
        vmodel.countUnReadMessage().observe(this, Observer {
            if(it==0) {
                dadgecounter.visibility = View.INVISIBLE
            }else{
                dadgecounter.visibility = View.VISIBLE
                dadgecounter.text = it.toString()
            }
        })
    }

    companion object{
        val TAG = "SalesViewPager"
    }
}
