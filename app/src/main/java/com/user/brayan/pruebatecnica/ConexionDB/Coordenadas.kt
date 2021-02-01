package com.user.brayan.pruebatecnica.ConexionDB

import android.provider.BaseColumns

object Coordenadas {
    object CoordenadasEntry : BaseColumns {
        const val table = "coordenadas"
        const val _ID = "_ID"
        const val latitud = "latitud"
        const val longitud = "longitud"
        const val altitud = "altitud"
        const val fecha = "fecha"
    }


}