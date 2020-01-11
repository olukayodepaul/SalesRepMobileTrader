package com.mobbile.paul.ui.attendant


import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.model.AttendantParser
import com.mobbile.paul.model.ProductParser
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.getDate
import com.mobbile.paul.util.Util.setGeoFencing
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_resumption.*
import javax.inject.Inject

class Resumption : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: AttendantViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationRequest: LocationRequest

    private lateinit var nAdapter: ResumptionAdapter

    var mode: Int = 0
    var tmid: Int = 0
    var repid: Int = 0
    var outletlat: Double = 0.0
    var outletlng: Double = 0.0
    var sequenceno: Int = 0
    var distance: String = ""
    var duration: String = ""
    var customer_code: String = ""
    var sortid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumption)
        vmodel = ViewModelProviders.of(this, modelFactory)[AttendantViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initViews()
    }

    private fun initViews() {

        tmid = intent.getIntExtra("tmid", 0)
        repid = intent.getIntExtra("repid", 0)
        outletlat = intent.getDoubleExtra("outletlat", 0.0)
        outletlng = intent.getDoubleExtra("outletlng", 0.0)
        sequenceno = intent.getIntExtra("sequenceno", 0)
        distance = intent.getStringExtra("distance")!!
        duration = intent.getStringExtra("duration")!!
        customer_code = intent.getStringExtra("customer_code")!!
        sortid = intent.getIntExtra("sort", 0)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        view_pager.layoutManager = layoutManager
        view_pager!!.setHasFixedSize(true)

        vmodel.getRepBasket(customer_code).observe(this, observebasket)
        vmodel.attendantExchange().observe(this, observeAttendant)

        refresh_image.setOnClickListener {
            showProgressBar(true)
            vmodel.getRepBasket(customer_code).observe(this, observebasket)
        }

        backbtn.setOnClickListener {
            onBackPressed()
        }

        resumebtn.setOnClickListener {
            mode = 1
            showProgressBar(true)
            startLocationUpdates()
        }

        clockoutbtn.setOnClickListener {
            mode = 2
            showProgressBar(true)
            startLocationUpdates()
        }

    }

    private val observebasket = Observer<ProductParser> {
        val data: ProductParser = it
        when (it.status) {
            200 -> {
                showProgressBar(false)
                nAdapter = ResumptionAdapter(data.list!!)
                nAdapter.notifyDataSetChanged()
                view_pager.setItemViewCacheSize(data.list!!.size)
                view_pager.adapter = nAdapter
                refresh_image.visibility = View.INVISIBLE
            }
            else -> {
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "${it.msg} ${customer_code}", "Basket Error")
                refresh_image.visibility = View.VISIBLE
            }
        }
    }

    private val observeAttendant = Observer<AttendantParser> {
        when (it.status) {
            200 -> {
                showProgressBar(false)
                if (mode == 1) {
                    showMessageDialogWithoutIntent(this, "Successful", it.notis)
                } else {
                    showMessageDialogWithIntent(SalesViewPager(), this, "Successful", it.notis)
                }
            }
            else -> {
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "Error", it.notis)
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
            callback,
            Looper.myLooper()
        )
    }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        if (location.latitude.isNaN() && location.longitude.isNaN()) {
            showProgressBar(false)
            stoplocation()
            startLocationUpdates()
        } else {

            stoplocation()

            val checkCustomerOutlet: Boolean =
                setGeoFencing(location.latitude, location.longitude, outletlat, outletlng, 1)

            if (!checkCustomerOutlet) {
                //showProgressBar(false)
                //showMessageDialogWithoutIntent(this,"Location Error","You are not at the DEPOT. Thanks!")

                when (mode) {
                    1 -> {
                        vmodel.takeAttendant(
                            1, repid, outletlat, outletlng, location.latitude, location.longitude,
                            distance, duration, "${sequenceno}", getDate()
                        )
                    }
                    2 -> {
                        vmodel.takeAttendant(
                            2, repid, outletlat, outletlng, location.latitude, location.longitude,
                            distance, duration, "${sequenceno}", getDate()
                        )
                    }
                }

            } else {
                when (mode) {
                    1 -> {
                        vmodel.takeAttendant(
                            1, repid, outletlat, outletlng, location.latitude, location.longitude,
                            distance, duration, "${sequenceno}", getDate()
                        )
                    }
                    2 -> {
                        vmodel.takeAttendant(
                            2, repid, outletlat, outletlng, location.latitude, location.longitude,
                            distance, duration, "${sequenceno}", getDate()
                        )
                    }
                }
            }
        }
    }

    private fun stoplocation() {
        fusedLocationClient.removeLocationUpdates(callback)
    }

    companion object {
        var TAG = "AttendantBasket"
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
    }
}
