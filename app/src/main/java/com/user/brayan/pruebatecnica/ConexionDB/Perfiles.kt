package com.user.brayan.pruebatecnica.ConexionDB

import android.provider.BaseColumns

object Perfiles {
    object PerfilesEntry : BaseColumns {
        const val table = "perfiles"
        const val _ID = "_ID"
        const val nombre = "nombre"
        const val apellidos = "apellidos"
        const val foto = "foto"
    }
}