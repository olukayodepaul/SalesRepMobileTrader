package com.mobbile.paul.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.mobbile.paul.model.NotificationData
import com.mobbile.paul.salesrepmobiletrader.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt

object Util {

    const val sharePrefenceDataSave = "com.tm_for_reps.v3.1.2.user.info"

    fun setGeoFencing(
        currentLat: Double, currentLng: Double,
        customerLat: Double, customerLng: Double, distance:Int
    ): Boolean {
        val ky = 40000 / 360
        val kx = cos(PI * customerLat / 180.0) * ky
        val dx = abs(customerLng - currentLng) * kx
        val dy = abs(customerLat - currentLat) * ky

        if(distance==1) {
            return sqrt(dx * dx + dy * dy) <= 1.000 // 100 meters//->0.050 is 50meters..using two kilometer 1 for one kilometer
        }else {
            return sqrt(dx * dx + dy * dy) <= 1.000 // 20 meters//..using two kilometer
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String{
        return SimpleDateFormat("HH:mm:ss").format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }

    fun showMessageDialogWithoutIntent(context: Context, title: String, msg: String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("Ok") { _, _ ->
            }
        val dialog = builder.create()
        dialog.show()
    }

    fun showMessageDialogWithIntent(activity: Activity, context: Context, title: String, msg: String?) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setCancelable(false)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setNegativeButton(
                "OK"
            ) { _, _ ->
                val intent = Intent(context, activity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context.startActivity(intent)
            }
        val dialog = builder.create()
        dialog.show()
    }

    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    fun intentWithFinish(context:Context, activity: Activity) {
        val intent = Intent(context, activity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun intentWithWithoutFinish(context:Context, activity: Activity) {
        val intent = Intent(context, activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    fun showProgressBar(visible: Boolean, base_progress_bar:View) {
        base_progress_bar.visibility =
            if (visible)
                View.VISIBLE
            else
                View.INVISIBLE
    }

    fun startGoogleMapIntent(ctx: Context, ads: String, mode: Char, avoid: Char): Any {
        val uri = Uri.parse("google.navigation:q=$ads&mode=$mode&avoid=$avoid")
        val mIntent = Intent(Intent.ACTION_VIEW, uri)
        mIntent.`package` = "com.google.android.apps.maps"
        return if (mIntent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(mIntent)
            true
        } else
            false
    }

    var onRatingRequired: MutableLiveData<NotificationData>? = null // observed in current tasks fragment, thrown when confirmation comes



}