package com.mobbile.paul.ui.login


import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
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
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mobbile.paul.model.LoginExchange
import com.mobbile.paul.model.NotificationData
import com.mobbile.paul.ui.modules.Modules
import com.mobbile.paul.util.Util.intentWithFinish
import com.mobbile.paul.util.Util.onRatingRequired
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: LoginViewModel

    private var preferences: SharedPreferences? = null

    var date = ""

    var getCurrentTokenFromDevice = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        receiveDeviceToken()
        vmodel = ViewModelProviders.of(this, modelFactory)[LoginViewModel::class.java]
        preferences = getSharedPreferences(sharePrefenceDataSave, Context.MODE_PRIVATE)
        date = preferences!!.getString("preferencesDate", "")!!
        initValues()

        /*
        onRatingRequired = MutableLiveData()
        onRatingRequired?.observe(this, Observer {
           Log.d(TAG,"this is message ${it.notis} ${it.status}")
        })
        */
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
                Log.d(TAG, "$it.employee_id")
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
            vmodel.Login("aYxT8Qz", "6990", "357574094971200", date, getCurrentTokenFromDevice)
            /*vmodel.Login(
                username,
                password,
                tel.getImei(0),
                date
            )*/
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
        if (ex.sep == 1) {
            Log.d(TAG, ex.employee_id.toString())
            preferences!!.edit().apply()
            val editor = preferences!!.edit()
            editor.clear()
            editor.putString("preferencesDate", ex.date)
            editor.putInt("preferencesEmployeeID", ex.employee_id)
            editor.putString("preferencesEmployeeName", ex.name)
            editor.putInt("preferencesEmployeeRegionId", ex.region_id)
            editor.apply()
            intentWithFinish(this, Modules())
            finish()
        } else {
            intentWithFinish(this, Modules())
            finish()
        }
    }

    companion object {
        const val DEVICE_STATE_PERMISSION = 101
        val TAG = "MainActivityassaAS"
    }

    private fun receiveDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result?.token
                getCurrentTokenFromDevice = token.toString()
            })
    }


}
