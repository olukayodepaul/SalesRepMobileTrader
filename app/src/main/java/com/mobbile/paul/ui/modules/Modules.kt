package com.mobbile.paul.ui.modules


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.model.modulesEntity
import com.mobbile.paul.ui.message.UsersList
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import kotlinx.android.synthetic.main.activity_modules.*
import javax.inject.Inject

class Modules : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: ModulesViewModel

    private lateinit var mAdapter: modulesAdapter

    lateinit var mLocationManager: LocationManager

    private var hasGps = false

    private var preferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modules)
        vmodel = ViewModelProviders.of(this, modelFactory)[ModulesViewModel::class.java]
        vmodel.getModules().observe(this, ObserversModulesResult)
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        requestLocationPermission()
    }

    fun initViews() {
        confirmNewMessages()
        tv_outlet_name.text = preferences!!.getString("preferencesEmployeeName","")!!
        module_recycler.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        module_recycler.layoutManager = layoutManager
    }

    val ObserversModulesResult = Observer<List<modulesEntity>> {
        if (it.isNotEmpty()) {
            showProgressBar(false)
            val list: List<modulesEntity> = it
            mAdapter = modulesAdapter(list, this,::modulesAdapterItems)
            mAdapter.notifyDataSetChanged()
            module_recycler.adapter = mAdapter
        }else {
            Util.showMessageDialogWithoutIntent(this, "Error","No Module assign to you")
        }
    }

    private fun modulesAdapterItems(item : modulesEntity) {
        var intent: Intent?
        when (item.nav) {
            1 -> {
                intent = Intent(this, SalesViewPager::class.java)
                startActivity(intent)
            }
            3 -> {
                intent = Intent(this, UsersList::class.java)
                startActivity(intent)
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    GPSPermissionRationaleAlert()
                }else{
                    mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if(hasGps) {
                        initViews()
                    }else {
                        callGpsIntent()
                    }
                }
            }
        }
    }

    private fun callGpsIntent() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, RC_ENABLE_LOCATION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_ENABLE_LOCATION -> {
                checkGpsEnabledAndPrompt()
            }
        }
    }

    private fun checkGpsEnabledAndPrompt() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (hasGps) {
            initViews()
        }else {
            GPSRationaleEnable()
        }
    }

    private fun GPSRationaleEnable() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!hasGps) {
            val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
            builder.setMessage("Your location need to be put On")
                .setTitle("GPS Enable")
                .setCancelable(false)
                .setNegativeButton("OK") { _, _ ->
                    callGpsIntent()
                }
            val dialog  = builder.create()
            dialog.show()
        }else {
            initViews()
        }
    }

    private fun GPSPermissionRationaleAlert() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogDanger)
        builder.setMessage("Without allowing Location permission, this application will not work for you")
            .setTitle("Location Permission")
            .setCancelable(false)
            .setNegativeButton("OK") { _, _ ->
                requestLocationPermission()
            }
        val dialog  = builder.create()
        dialog.show()
    }

    //count unread messages
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

    companion object {
        private var TAG = "Modules"
        const val RC_ENABLE_LOCATION = 1
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1235
    }

}
