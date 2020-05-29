package com.mobbile.paul



import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.login.MainActivity


class CloudMessage : FirebaseMessagingService() {

    lateinit var builder: NotificationCompat.Builder

    lateinit var notificationManager: NotificationManager

    lateinit var notificationChannel: NotificationChannel

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val intent =  Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        if (p0.notification != null) {
            val messageBody = p0.notification!!.body!!
            val messageTitle = p0.notification!!.title!!
            passMessageToActivity(messageTitle, messageBody, intent)
        }
    }

    private fun passMessageToActivity(title: String, body: String, intent:Intent) {

        /*val pendingindent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, FLAG_CANCEL_CURRENT or FLAG_UPDATE_CURRENT)

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //create a notification manager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel= NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH )
            notificationChannel.description = description
            notificationChannel.lightColor= Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logoss)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLights(15859712, 400, 600)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(soundUri, AudioManager.STREAM_ALARM)
                .setContentTitle(title)
                .setContentText(body)
                //.setContentIntent(pendingindent)
                //.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }else{
            builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logoss)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLights(15859712, 400, 600)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(soundUri, AudioManager.STREAM_ALARM)
                .setContentTitle(title)
                .setContentText(body)
            //.setContentIntent(pendingindent)
            //.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }
        notificationManager.notify(0, builder.build())
*/
    }

    companion object {
        const val channelId = "com.mobbile.paul.salesrepmobiletrader"
        const val description = "Order Notification"
    }
}