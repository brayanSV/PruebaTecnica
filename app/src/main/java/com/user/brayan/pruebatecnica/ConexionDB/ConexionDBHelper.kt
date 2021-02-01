package com.user.brayan.pruebatecnica.ConexionDB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(sql_coordenadas)
        db?.execSQL(sql_perfil)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "PruebaTecnica.db"

        private const val sql_coordenadas =
            "CREATE TABLE IF NOT EXISTS ${Coordenadas.CoordenadasEntry.table} (" +
                    "${Coordenadas.CoordenadasEntry._ID} INTEGER PRIMARY KEY," +
                    "${Coordenadas.CoordenadasEntry.latitud} TEXT NOT NULL," +
                    "${Coordenadas.CoordenadasEntry.longitud} TEXT NOT NULL," +
                    "${Coordenadas.CoordenadasEntry.altitud} TEXT NOT NULL," +
                    "${Coordenadas.CoordenadasEntry.fecha} TEXT NOT NULL)"

        private const val sql_perfil =
            "CREATE TABLE IF NOT EXISTS ${Perfiles.PerfilesEntry.table} (" +
                    "${Perfiles.PerfilesEntry._ID} INTEGER PRIMARY KEY," +
                    "${Perfiles.PerfilesEntry.nombre} TEXT," +
                    "${Perfiles.PerfilesEntry.apellidos} TEXT," +
                    "${Perfiles.PerfilesEntry.foto} BLOB)"
    }
}