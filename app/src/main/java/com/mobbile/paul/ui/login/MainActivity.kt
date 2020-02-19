package com.mobbile.paul.ui.login


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mobbile.paul.BaseActivity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.util.Util.isInternetAvailable
import com.mobbile.paul.util.Util.sharePrefenceDataSave
import com.mobbile.paul.util.Util.showMessageDialogWithoutIntent
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.mobbile.paul.model.LoginExchange
import com.mobbile.paul.ui.modules.Modules
import com.mobbile.paul.ui.salesviewpagers.SalesViewPager
import com.mobbile.paul.util.Util.intentWithFinish
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: LoginViewModel

    private var preferences: SharedPreferences? = null

    var date = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vmodel = ViewModelProviders.of(this, modelFactory)[LoginViewModel::class.java]
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        date = preferences!!.getString("preferencesDate", "")!!
        initValues()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initValues() {
        showProgressBar(false)
        btn_login.setOnClickListener {
            if (!isInternetAvailable(this)) {
                showMessageDialogWithoutIntent(
                    this,
                    "Internet Error",
                    "Your Mobile Data is not active"
                )
            } else {
                showProgressBar(true)
                processLogin()
            }
        }
        vmodel.LoginObserver().observe(this, loginObservers)
    }

    val loginObservers = Observer<LoginExchange> {
        when (it.status) {
            200 -> {
                setSession(it)
            }
            400 -> {
                showProgressBar(false)
                showMessageDialogWithoutIntent(this, "Error", it.notification)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processLogin() {
        val permit = checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        val username: String = et_email.text.toString()
        val password: String = et_password.text.toString()
        val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (username.isEmpty() && password.isEmpty()) {
            showProgressBar(false)
            showMessageDialogWithoutIntent(
                this,
                "Form Error",
                "Please enter both username and password"
            )
        } else if (permit == PackageManager.PERMISSION_DENIED) {
            imeiRequest()
        } else {
            //vmodel.Login("iDaCu5a", "5169", "351736102823741", date)
            vmodel.Login(
                username,
                password,
                tel.getImei(0),
                date
            )
        }
    }

    private fun imeiRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_PHONE_STATE), DEVICE_STATE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            DEVICE_STATE_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        applicationContext,
                        "imei permission deny, please allow",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        showProgressBar(false)
    }

    private fun setSession(ex: LoginExchange) {
        showProgressBar(false)
        if(ex.sep==1) {
            preferences!!.edit().apply()
            val editor = preferences!!.edit()
            editor.clear()
            editor.putString("preferencesDate", ex.date)
            editor.putInt("preferencesEmployeeID", ex.employee_id)
            editor.putString("preferencesEmployeeName", ex.name)
            editor.putInt("preferencesEmployeeRegionId",ex.region_id)
            editor.apply()
            intentWithFinish(this, Modules())
            finish()
        }else{
            intentWithFinish(this, Modules())
            finish()
        }
    }

    companion object {
        const val DEVICE_STATE_PERMISSION = 101
    }
}
