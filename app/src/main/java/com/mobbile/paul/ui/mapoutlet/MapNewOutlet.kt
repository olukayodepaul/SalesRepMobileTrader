package com.mobbile.paul.ui.mapoutlet


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.OutletUpdateParser
import com.mobbile.paul.model.spinersEntity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.outletupdate.CustomerClassSpinnerAdapter
import com.mobbile.paul.ui.outletupdate.OutletTypeSpinnerAdapter
import com.mobbile.paul.ui.outletupdate.OutletUpdate
import com.mobbile.paul.ui.outletupdate.PreferedLanguageSpinnerAdapter
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util
import com.mobbile.paul.util.Util.isInternetAvailable
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_map_new_outlet.*
import kotlinx.android.synthetic.main.activity_map_new_outlet.address_edit
import kotlinx.android.synthetic.main.activity_map_new_outlet.backbtn
import kotlinx.android.synthetic.main.activity_map_new_outlet.contact_name_edit
import kotlinx.android.synthetic.main.activity_map_new_outlet.custClass
import kotlinx.android.synthetic.main.activity_map_new_outlet.customer_name_edit
import kotlinx.android.synthetic.main.activity_map_new_outlet.outlettypeedit
import kotlinx.android.synthetic.main.activity_map_new_outlet.phone_number_edit
import kotlinx.android.synthetic.main.activity_map_new_outlet.preflang
import kotlinx.android.synthetic.main.activity_map_new_outlet.registerBtn
import kotlinx.android.synthetic.main.activity_outlet_update.*
import javax.inject.Inject

class MapNewOutlet : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: MapNewOutletViewModel

    lateinit var customerClassAdapter: CustomerClassSpinnerAdapter

    lateinit var preferedLangAdapter: PreferedLanguageSpinnerAdapter

    lateinit var outletTypeAdapter: OutletTypeSpinnerAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var hasGps = false

    lateinit var mLocationManager: LocationManager

    lateinit var locationRequest: LocationRequest

    private var preferences: SharedPreferences? = null

    var rep_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_new_outlet)
        vmodel = ViewModelProviders.of(this, modelFactory)[MapNewOutletViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        rep_id = preferences!!.getInt("preferencesEmployeeID",0)
        initViews()
    }

    private fun initViews(){

        customerClassAdapter = CustomerClassSpinnerAdapter()
        preferedLangAdapter = PreferedLanguageSpinnerAdapter()
        outletTypeAdapter = OutletTypeSpinnerAdapter()

        backbtn.setOnClickListener {
            onBackPressed()
        }
        vmodel.fetchSpinners().observe(this, languageObserver)

        registerBtn.setOnClickListener {
            if (!isInternetAvailable(this)) {
                showMessageDialogWithoutIntent(this, "Network Error", "No Internet Connection, Thanks!")
            } else {
                when {
                    customer_name_edit.text.toString() == "" -> {
                        showMessageDialogWithoutIntent(this, "Entering Error", "Please Enter Customer Name")
                    }
                    contact_name_edit.text.toString()=="" -> {
                        showMessageDialogWithoutIntent(this,"Entering Error", "Please Enter Contact Name")
                    }
                    address_edit.text.toString()=="" -> {
                        showMessageDialogWithoutIntent(this, "Entering Error", "Please Enter Address")
                    }
                    else -> {
                        showProgressBar(true)
                        getGps()
                    }
                }
            }
        }
    }

    val languageObserver = Observer<List<spinersEntity>> {

        if (it != null) {
            val outletClassList = ArrayList<String>()
            val preLangsList = ArrayList<String>()
            val outletTypeList = ArrayList<String>()
            if (customerClassAdapter.size() > 0) {
                customerClassAdapter.clear()
            }
            if (preferedLangAdapter.size() > 0) {
                preferedLangAdapter.clear()
            }
            if (outletTypeAdapter.size() > 0) {
                outletTypeAdapter.clear()
            }
            for (i in it.indices) {
                when (it[i].sep) {
                    1 -> {
                        outletClassList.add(it[i].name)
                        customerClassAdapter.add(it[i].id, it[i].name)
                    }
                    2 -> {
                        preLangsList.add(it[i].name)
                        preferedLangAdapter.add(it[i].id, it[i].name)
                    }
                    3 -> {
                        outletTypeList.add(it[i].name)
                        outletTypeAdapter.add(it[i].id, it[i].name)
                    }
                }
            }

            val mOutletClass = ArrayAdapter(this, android.R.layout.simple_spinner_item, outletClassList)
            mOutletClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            custClass!!.adapter = mOutletClass

            val mPreferedLang = ArrayAdapter(this, android.R.layout.simple_spinner_item, preLangsList)
            mPreferedLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            preflang!!.adapter = mPreferedLang

            val mOutletType = ArrayAdapter(this, android.R.layout.simple_spinner_item, outletTypeList)
            mOutletType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            outlettypeedit!!.adapter = mOutletType

            showProgressBar(false)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getGps() {

        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)

        val accessPermissionStatus =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermissionStatus =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (accessPermissionStatus == PackageManager.PERMISSION_DENIED
            && coarsePermissionStatus == PackageManager.PERMISSION_DENIED
        ) {
            showProgressBar(false)
            requestLocationPermission()

        } else if (!hasGps) {
            showProgressBar(false)
            callGpsIntent()
        } else if (available == ConnectionResult.API_UNAVAILABLE) {
            showMessageDialogWithoutIntent(this, "Google Play","Please Install and setup Google Play service")
        } else {
            startLocationUpdates()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@MapNewOutlet,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestLocationPermission()
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
                mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                hasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (!hasGps) {
                    callGpsIntent()
                }
            }
        }
    }
    fun startLocationUpdates() {

        locationRequest = LocationRequest()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(builder.build())

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            LocationFinder,
            Looper.myLooper()
        )
    }

    private val LocationFinder = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChangedForClose(locationResult.lastLocation)
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun onLocationChangedForClose(location: Location) {

        if(location.latitude.isNaN() && location.longitude.isNaN()) {


            stoplocationUpdates()
            startLocationUpdates()

        }else{

            stoplocationUpdates()
            val outletName = customer_name_edit.text.toString()
            val contactName = contact_name_edit.text.toString()
            val address = address_edit.text.toString()
            val phones = phone_number_edit.text.toString()
            val outletClass = customerClassAdapter.getValueId(custClass.selectedItem.toString())
            val prefLang = preferedLangAdapter.getValueId(preflang.selectedItem.toString())
            val outletTypeId = outletTypeAdapter.getValueId(outlettypeedit.selectedItem.toString())

            vmodel.mapOutlet(rep_id,rep_id,location.latitude, location.longitude, outletName, contactName, address, phones,
                outletClass, prefLang, outletTypeId
            ).observe(this, mapnewoutlet)
        }
    }

    private fun stoplocationUpdates() {
        fusedLocationClient.removeLocationUpdates(LocationFinder)
    }

    val mapnewoutlet = Observer<OutletUpdateParser> {
        when(it.status) {
            200 -> {
                showProgressBar(false)
                showMessageDialogWithIntent(
                    SalesViewPager(),
                    this,
                    "SUCCESSFUL",
                    "Outlet Successfully Map"
                )
            }
            else -> {
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "Error", it.notis )
            }
        }
    }

    companion object {
        var REQUEST_PERMISSIONS_REQUEST_CODE = 1000
        var TAG = "OutletUpdate"
        var RC_ENABLE_LOCATION = 1000
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
    }


}
