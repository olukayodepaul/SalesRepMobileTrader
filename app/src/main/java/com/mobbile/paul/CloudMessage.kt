package com.mobbile.paul


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobbile.paul.ui.login.MainActivity

class CloudMessage: FirebaseMessagingService() {

    var promoId = ""
    var promo = ""
    var promoUnit = ""
    var body = ""
    var title = ""
    var RC_INTENT = 100
    var CHANNEL_ID = "appx0f"


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        //if the incoming message is a data, get the content
        if(p0.data.isNotEmpty()) {

            promoId = p0.data.getValue("promoId")
            promo = p0.data.getValue("promo")
            promoUnit = p0.data.getValue("promoUnit")

            intent.putExtra("promoId",promoId)
            intent.putExtra("promo",promo)
            intent.putExtra("promoUnit",promoUnit)
            intent.putExtra("type",0)

            sendNotif("Today Promo!!","$promo $promoUnit", intent)
        }

        //if the incoming message is a notification, get the content
        if(p0.notification!=null) {

            body = p0.notification!!.body!!
            title = p0.notification!!.title!!

            intent.putExtra("title",body)
            intent.putExtra("body",title)
            intent.putExtra("type",1)

            sendNotif(title,"$body", intent)

        }else{
            Log.d(TAG, "error access the information")
        }
    }

    fun sendNotif(title: String, body:String, intent: Intent) {

        //call pending intent in the application
        val pendingindent: PendingIntent = PendingIntent.getActivity(this, RC_INTENT,
            intent, PendingIntent.FLAG_ONE_SHOT)

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
            val mChannel= NotificationChannel(CHANNEL_ID, "appx0f", NotificationManager.IMPORTANCE_HIGH )
            mChannel.description = "App x0f"
            mChannel.setSound(ringtoneUri, audioAttributes)
            notificationManeger.createNotificationChannel(mChannel)
        }

        //build a notification
        /*val notificationBuilder: Notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_2b_01)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.logo_2b_01))
            .setContentText(title)
            .setContentText(body)
            .setSound(ringtoneUri)
            .setContentIntent(pendingindent)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setNumber(0)
            .build()
        notificationManeger.notify(RC_INTENT, notificationBuilder)*/

    }

    companion object{
        val TAG = "DNCJDNCJDNCJDNJDN"
    }

}