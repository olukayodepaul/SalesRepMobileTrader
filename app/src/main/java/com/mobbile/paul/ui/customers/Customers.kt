package com.mobbile.paul.ui.customers


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.mobbile.paul.model.*
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.details.Details
import com.mobbile.paul.ui.entries.Entries
import com.mobbile.paul.ui.mapoutlet.MapNewOutlet
import com.mobbile.paul.ui.outletupdate.OutletUpdate
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.getDate
import com.mobbile.paul.util.Util.setGeoFencing
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithIntent
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import com.mobbile.paul.util.Util.showProgressBar
import com.mobbile.paul.util.Util.startGoogleMapIntent
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_sales.*
import java.util.*
import javax.inject.Inject


class Customers: DaggerFragment() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: CustomerViewModel

    private lateinit var nAdapter: CustomersAdapter

    private var preferences: SharedPreferences? = null

    var employee_id = 0

    private var mode = 0

    private lateinit var dataFromAdapter: customersEntity

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var locationRequest: LocationRequest


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sales, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmodel = ViewModelProviders.of(this, modelFactory)[CustomerViewModel::class.java]
        preferences = activity!!.getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        employee_id = preferences!!.getInt("preferencesEmployeeID", 0)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        showProgressBar(true, base_progress_bar)
        initViews()
    }

    fun initViews() {
        vmodel.fetchsAllCustomers(employee_id)
        _r_view_pager.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.requireContext())
        _r_view_pager.layoutManager = layoutManager
        vmodel.CustomersObserver().observe(this, observeRepBbasket)
        vmodel.CloseOutletObserver().observe(this, observeCloseOutlets)
        vmodel.asycnOutlet().observe(this, observeDetailsChange)

        fab.setOnClickListener {
            val intent = Intent(this.requireContext(), MapNewOutlet::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private val observeDetailsChange = Observer<OutletUpdateParser> {
        if (it.status == 200) {
            showProgressBar(false, base_progress_bar)
            showMessageDialogWithIntent(
                SalesViewPager(),
                this.requireContext(),
                "Successful",
                "Customer data successfully synchronise"
            )
        } else {
            showProgressBar(false, base_progress_bar)
            showMessageDialogWithoutIntent(
                this.requireContext(),
                "Outlet Close Error",
                "Customer data fail to synchronise"
            )
        }
    }

    private val observeRepBbasket = Observer<CustomerExchage> {
        when (it.status) {
            200 -> {
                showProgressBar(false, base_progress_bar)
                TmSalesRepCustomer(it.allcustomers!!)
            }
            else -> {
                showProgressBar(false, base_progress_bar)
                showMessageDialogWithoutIntent(this.requireContext(), "Error", it.msg)
            }
        }
    }

    private val observeCloseOutlets = Observer<AttendantParser> {
        when (it.status) {
            200 -> {
                showProgressBar(false, base_progress_bar)
                showMessageDialogWithIntent(
                    SalesViewPager(),
                    this.requireContext(),
                    "Successful",
                    it.notis
                )
            }
            else -> {
                showProgressBar(false, base_progress_bar)
                showMessageDialogWithoutIntent(
                    this.requireContext(),
                    "Outlet Close Error",
                    it.notis
                )
            }
        }
    }

    fun TmSalesRepCustomer(data: List<customersEntity>) {
        showProgressBar(false, base_progress_bar)
        nAdapter = CustomersAdapter(data, this.requireContext(), this::partItemClicked)
        nAdapter.notifyDataSetChanged()
        _r_view_pager.setItemViewCacheSize(data.size)
        _r_view_pager.adapter = nAdapter
    }

    private fun partItemClicked(partItem: customersEntity, separator: Int) {
        when (separator) {
            100 -> {
                showProgressBar(true, base_progress_bar)
                mode = 1
                dataFromAdapter = partItem
                startLocationRequest()
            }
            200 -> {
                val dmode = partItem.mode.single()
                val destination = "${partItem.latitude},${partItem.longitude}"
                startGoogleMapIntent(this.requireContext(), destination, dmode, 't')
            }
            300 -> {
                dataFromAdapter = partItem
                val intent = Intent(this.requireContext(), OutletUpdate::class.java)
                intent.putExtra("extra_item", dataFromAdapter)
                startActivity(intent)
            }
            400 -> {
                showProgressBar(true, base_progress_bar)
                mode = 2
                dataFromAdapter = partItem
                startLocationRequest()
            }
            500 -> {
                showProgressBar(true, base_progress_bar)
                dataFromAdapter = partItem
                asynchroniseDialogWithoutIntent()
            }
            600 -> {
                dataFromAdapter = partItem
                val intent = Intent(this.requireContext(), Details::class.java)
                intent.putExtra("urno", dataFromAdapter.urno)
                intent.putExtra("rep_id", dataFromAdapter.rep_id)
                intent.putExtra("outletname", dataFromAdapter.outletname)
                startActivity(intent)
            }
        }
    }


    private fun startLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this.requireContext())
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

    private fun onLocationChanged(location: Location) {
        if (location.latitude.isNaN() && location.longitude.isNaN()) {
            showProgressBar(false, base_progress_bar)
            stoplocation()
            startLocationRequest()
        } else {
            stoplocation()
            val checkCustomerOutlet: Boolean = setGeoFencing(location.latitude, location.longitude, dataFromAdapter.latitude, dataFromAdapter.longitude, 2)
            if (!checkCustomerOutlet) {
                showProgressBar(false, base_progress_bar)
                showMessageDialogWithoutIntent(this.requireContext(), "Location Error", "You are not at the corresponding outlet. Thanks!")
            } else {
                vmodel.validateOutletSequence(1, dataFromAdapter.sequenceno, location.latitude, location.longitude).observe(this, observeVisitSequence)
            }
        }
    }

    private val observeVisitSequence = Observer<SequenceExchage> {
        when (it.status) {
            200 -> {
                when (mode) {
                    1 -> {
                        showProgressBar(false, base_progress_bar)
                        val intent = Intent(this.requireContext(), Entries::class.java)
                        intent.putExtra("extra_item", dataFromAdapter)
                        intent.putExtra("currentlat", it.currentLat.toString()) //current lat
                        intent.putExtra("currentlng", it.currentLng.toString())
                        startActivity(intent)
                    }
                    2 -> {
                        vmodel.closeOutlet(
                            dataFromAdapter.rep_id,
                            it.currentLat.toString(),
                            it.currentLng.toString(),
                            dataFromAdapter.latitude.toString(),
                            dataFromAdapter.longitude.toString(),
                            dataFromAdapter.distance,
                            dataFromAdapter.duration,
                            dataFromAdapter.urno,
                            dataFromAdapter.sequenceno,
                            dataFromAdapter.auto,
                            getDate() + "${dataFromAdapter.rep_id}" + UUID.randomUUID().toString()
                        )
                    }
                }
            }
            300 -> {
                showProgressBar(false, base_progress_bar)
                showMessageDialogWithoutIntent(
                    this.requireContext(),
                    "Visit Error",
                    "Please follow the outlet visit sequence. Thanks!"
                )
            }
            else -> {
                showProgressBar(false, base_progress_bar)
                showMessageDialogWithoutIntent(
                    this.requireContext(),
                    "Attendant Error",
                    "Please resume and clock out before you proceed"
                )
            }
        }
    }

    private fun stoplocation() {
        fusedLocationClient.removeLocationUpdates(callback)
    }

    fun asynchroniseDialogWithoutIntent() {
        val builder = AlertDialog.Builder(this.requireContext(), R.style.AlertDialogDanger)
        builder.setMessage("Are you sure you want to synchronise ${dataFromAdapter.outletname} outlet data")
            .setTitle("Data Async")
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("Ok") { _, _ ->
                vmodel.CustometInfoAsync(dataFromAdapter.urno, dataFromAdapter.auto)
            }.setPositiveButton("NO"){ _, _ ->
                showProgressBar(false, base_progress_bar)
            }
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        private var TAG = "Customers"
        private const val INTERVAL: Long = 1 * 1000
        private const val FASTEST_INTERVAL: Long = 1 * 1000
    }
}
