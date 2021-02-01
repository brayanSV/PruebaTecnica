package com.user.brayan.pruebatecnica.Services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.user.brayan.pruebatecnica.R
import java.lang.Exception

class MyFirebaseMessagingService: FirebaseMessagingService() {
    var channel_ID = "com.user.brayan.pruebatecnica"
    val notification_ID = 9604

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("datos", "--- crea la notificacion ---")

        if (remoteMessage.notification != null) {
            showNotification(applicationContext, remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)

        Log.e("datos", "p0: " + p0 + ", E1: " + p1.message)
    }

    fun showNotification(context: Context, titulo: String?, mensaje: String?) {
        try {
            val notification: Notification
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = NotificationCompat.Builder(context, channel_ID)
                    .setOngoing(true)
                    .setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .build()

                val notificationChannel = NotificationChannel(channel_ID, titulo, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(notificationChannel)
                notificationManager.notify(notification_ID, notification)
            } else {
                notification = NotificationCompat.Builder(context)
                    .setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(notification_ID, notification)
            }
        } catch (ex : Exception) {
            Log.e("datos", "ex: " + ex.message )
        }

    }
}