package com.user.brayan.pruebatecnica.Services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.user.brayan.pruebatecnica.ConexionDB.ConexionDBHelper
import com.user.brayan.pruebatecnica.ConexionDB.Coordenadas
import com.user.brayan.pruebatecnica.R
import java.text.SimpleDateFormat
import java.util.*

class ServiceLocation : Service()  {
    private val notification_ID = 9603
    private val channel_ID = "com.user.brayan.pruebatecnica"
    private lateinit var notificationManager : NotificationManager

    private var locationRequest : LocationRequest? = null
    private var locationCallback : LocationCallback? = null
    private var fusedLocation: FusedLocationProviderClient? = null
    private val update_time_milisecons : Long = 10000

    private val binder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startForeground(
            notification_ID,
            createNotification(getString(R.string.message_location_notification))
        )
        return START_NOT_STICKY
    }

    private fun createNotification(message: String) : Notification {
        val notification: Notification

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(this, channel_ID)
                .setContentText(getString(R.string.title_location_notification))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContentText(message)
                .build()

            val notificationChannel = NotificationChannel(
                channel_ID,
                getString(R.string.title_location_notification),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            notification = NotificationCompat.Builder(this)
                .setContentText(getString(R.string.title_location_notification))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .build()
        }

        return notification
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest().apply {
            interval = update_time_milisecons
            fastestInterval = update_time_milisecons/2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                if (locationResult != null){
                    onNewLocation(locationResult.lastLocation)
                }
            }
        }

        try {
            fusedLocation?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {

        }
    }

    private fun onNewLocation(location: Location) {
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        var message = "(" + location.latitude + "," + location.longitude + ")"
        notificationManager.notify(notification_ID, createNotification(message))

        val conexionDBHelper = ConexionDBHelper(this)
        val db = conexionDBHelper.writableDatabase

        val date = Date(location.time)
        val formatDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val values = ContentValues().apply {
            put(Coordenadas.CoordenadasEntry.latitud, location.latitude)
            put(Coordenadas.CoordenadasEntry.longitud, location.longitude)
            put(Coordenadas.CoordenadasEntry.altitud, location.altitude)
            put(Coordenadas.CoordenadasEntry.fecha, formatDate.format(date))
        }

        val newRowID = db?.insert(Coordenadas.CoordenadasEntry.table, null, values)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (fusedLocation != null) {
            fusedLocation?.removeLocationUpdates(locationCallback)
        }
    }

    inner class LocalBinder: Binder() {
        fun getService(): ServiceLocation = this@ServiceLocation
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    companion object {
        private const val PACKAGE_NAME = "com.user.brayan.pruebatecnica.Services"
        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST = "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"
    }
}