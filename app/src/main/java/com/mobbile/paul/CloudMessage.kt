package com.mobbile.paul



import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.login.MainActivity


class CloudMessage: FirebaseMessagingService() {

    var RC_INTENT = 100
    var CHANNEL_ID = "salesrepmobiletrader"

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if(p0.notification!=null) {
            val messageBody = p0.notification!!.body!!
            val messageTitle = p0.notification!!.title!!
            passMessageToActivity(messageTitle, messageBody, intent)
        }
    }

    private fun passMessageToActivity(title: String, body:String, intent: Intent){

        val pendingindent: PendingIntent = PendingIntent.getActivity(this, RC_INTENT, intent, PendingIntent.FLAG_ONE_SHOT)

        //choose ringing tone
        val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        //create a notification manager
        val notificationManeger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //for android 8 sdk
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel= NotificationChannel(CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH )
            mChannel.description = "Notification"
            mChannel.setSound(ringtoneUri, audioAttributes)
            notificationManeger.createNotificationChannel(mChannel)
        }

        //build a notification
        val notificationBuilder: Notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.logoss)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.logoss))
            .setContentText(title)
            .setContentText(body)
            .setSound(ringtoneUri)
            .setContentIntent(pendingindent)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setNumber(0)
            .build()
        notificationManeger.notify(RC_INTENT, notificationBuilder)
    }

    companion object {
        val TAG = "DNCJDNCJDNCJDNJDN"
    }
}