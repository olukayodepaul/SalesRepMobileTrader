package com.mobbile.paul.ui.outletupdate


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.mobbile.paul.model.customersEntity
import com.mobbile.paul.model.spinersEntity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.isInternetAvailable
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_outlet_update.*
import javax.inject.Inject

class OutletUpdate : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: OutletUpdateViewModel

    lateinit var customerClassAdapter: CustomerClassSpinnerAdapter

    lateinit var preferedLangAdapter: PreferedLanguageSpinnerAdapter

    lateinit var outletTypeAdapter: OutletTypeSpinnerAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var hasGps = false

    lateinit var mLocationManager: LocationManager

    lateinit var locationRequest: LocationRequest

    private lateinit var customers: customersEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outlet_update)
        vmodel = ViewModelProviders.of(this, modelFactory)[OutletUpdateViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        customers = intent.extras!!.getParcelable("extra_item")!!
        initViews()
    }

    private fun initViews(){

        customer_name_edit.setText(customers.outletname)
        contact_name_edit.setText(customers.contactname)
        address_edit.setText(customers.outletaddress)
        phone_number_edit.setText(customers.contactphone)

        customerClassAdapter = CustomerClassSpinnerAdapter()
        preferedLangAdapter = PreferedLanguageSpinnerAdapter()
        outletTypeAdapter = OutletTypeSpinnerAdapter()

        backbtn.setOnClickListener {
            onBackPressed()
        }

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

        vmodel.fetchSpinners().observe(this, languageObserver)
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
            custClass!!.setSelection(customerClassAdapter.getIndexById(customers.outletclassid))

            val mPreferedLang = ArrayAdapter(this, android.R.layout.simple_spinner_item, preLangsList)
            mPreferedLang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            preflang!!.adapter = mPreferedLang
            preflang!!.setSelection(preferedLangAdapter.getIndexById(customers.outletlanguageid))

            val mOutletType = ArrayAdapter(this, android.R.layout.simple_spinner_item, outletTypeList)
            mOutletType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            outlettypeedit!!.adapter = mOutletType
            outlettypeedit!!.setSelection(outletTypeAdapter.getIndexById(customers.outlettypeid))
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
        ActivityCompat.requestPermissions(this@OutletUpdate, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
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

            vmodel.updateOutlet(customers.rep_id, customers.urno, location.latitude, location.longitude, outletName, contactName, address, phones,
                outletClass, prefLang, outletTypeId
            ).observe(this, updateObserver)
        }
    }

    private fun stoplocationUpdates() {
        fusedLocationClient.removeLocationUpdates(LocationFinder)
    }

    val updateObserver = Observer<OutletUpdateParser> {

        when(it.status) {
            200 -> {
                showMessageDialogWithIntent(SalesViewPager(), this, "SUCCESSFUL", "Outlet Successfully updated")
            }
            else -> {
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
