package com.orpatservice.app.ui.admin.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orpatservice.app.R
import com.orpatservice.app.ui.SplashActivity
import com.orpatservice.app.ui.admin.dashboard.DashboardActivity
import com.orpatservice.app.utils.Constants



class YourNotificationServiceExtension : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {

        if (message.getData().size > 0) {
            println("Message data payload: " + message.getData().toString())

            val data: Map<String, String> = message.getData()

            val title = data["page_name"]
            sendNotification(this, title);
            openPage(title!!)

        }
    }

    fun sendNotification(context: Context, title: String?) {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_MUTABLE)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setContentText(title)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())


    }
    private fun openPage(page: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra(Constants.MODULE_TYPE, page)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
}