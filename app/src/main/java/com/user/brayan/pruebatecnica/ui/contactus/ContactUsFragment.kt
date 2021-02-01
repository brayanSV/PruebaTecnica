package com.user.brayan.pruebatecnica.ui.contactus

import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.user.brayan.pruebatecnica.ConexionDB.ConexionDBHelper
import com.user.brayan.pruebatecnica.ConexionDB.Coordenadas
import com.user.brayan.pruebatecnica.R
import com.user.brayan.pruebatecnica.Services.ServiceLocation
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ContactUsFragment : Fragment(), OnMapReadyCallback, View.OnClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var btnDownLoad : MaterialButton
    private lateinit var serviceLocation : ServiceLocation
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as ServiceLocation.LocalBinder
            serviceLocation = binder.getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_contactenos, container, false)
        btnDownLoad = root.findViewById(R.id.btnDownLoad);
        btnDownLoad.setOnClickListener(this)

        var mapView = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapView?.getMapAsync(this)

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()

        var intent = Intent(context, ServiceLocation::class.java)
        context?.startForegroundService(intent)
    }

    override fun onStart() {
        super.onStart()

        var intent = Intent(context, ServiceLocation::class.java)
        context?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ServiceLocation.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val location = intent?.getParcelableExtra<Location>(
                ServiceLocation.EXTRA_LOCATION
            )

            if (location != null) {
                val updateLocation = LatLng(location.latitude, location.longitude)

                mMap.clear()
                var icon = BitmapDescriptorFactory.fromResource(R.drawable.poi_orange)
                mMap.addMarker(MarkerOptions().position(updateLocation)).setIcon(icon)

                val cameraPosition = CameraPosition.Builder()
                    .target(updateLocation)
                    .zoom(13f)
                    .bearing(0f)
                    .tilt(30f)
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.btnDownLoad -> {
                openDirSaveFile()
            }
        }
    }

    private fun openDirSaveFile() {
        val date = Date()
        val formatDate = SimpleDateFormat("yyyyMMddHHmmss")

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/text"
            putExtra(Intent.EXTRA_TITLE, "Log_coordenadas_" + formatDate.format(date) + ".txt")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DOWNLOADS)
        }

        startActivityForResult(intent, CREATE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataFile: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataFile)

        if (requestCode == CREATE_FILE) {
            val conexionDBHelper = ConexionDBHelper(requireContext())
            val db = conexionDBHelper.readableDatabase

            val projection = arrayOf(
                Coordenadas.CoordenadasEntry._ID,
                Coordenadas.CoordenadasEntry.latitud,
                Coordenadas.CoordenadasEntry.longitud,
                Coordenadas.CoordenadasEntry.altitud,
                Coordenadas.CoordenadasEntry.fecha
            )

            val cursor = db.query(
                Coordenadas.CoordenadasEntry.table,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            try {
                val outputStream = context?.contentResolver?.openOutputStream(dataFile?.data!!)
                val writeFile = BufferedWriter(OutputStreamWriter(outputStream))

                with(cursor) {
                    while (moveToNext()) {
                        val latitud = getString(getColumnIndexOrThrow(Coordenadas.CoordenadasEntry.latitud))
                        val longitud = getString(getColumnIndexOrThrow(Coordenadas.CoordenadasEntry.longitud))
                        val altitud = getString(getColumnIndexOrThrow(Coordenadas.CoordenadasEntry.altitud))
                        val fecha = getString(getColumnIndexOrThrow(Coordenadas.CoordenadasEntry.fecha))

                        writeFile.write(fecha + " - Latitud: " + latitud + ", Longitud: " + longitud + ", Altitud: " + altitud + "\r\n")
                    }
                }

                writeFile.flush()
                writeFile.close()
            } catch (ex: Exception) {
                Log.e("datos", ex.message.toString());
            }
        }
    }

    companion object {
        const val CREATE_FILE = 1
    }
}