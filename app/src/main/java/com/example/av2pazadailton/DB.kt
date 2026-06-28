package com.example.av2pazadailton

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DB (context: Context) :
        SQLiteOpenHelper ( context, "db_av2", null, 4){
    init{
        Log.d("Adailton_av2", "DB-> init()")
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("Adailton_av2", "DB->onCreate() = " + db.version.toString())

        db.execSQL(
            "CREATE TABLE usuario ( " +
                "cod_usuario INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "nome_usuario TEXT, " +
                "email_usuario TEXT, " +
                "senha_usuario TEXT ); "
        )
        db.execSQL(
            "CREATE TABLE clientes ( " +
                "cod_cliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome_cliente TEXT, " +
                "email_cliente TEXT, " +
                "telefone_cliente TEXT, " +
                "favorito_cliente INTEGER );"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("Adailton_av2", "DB->onUpgrade() oldVersion:$oldVersion newVersion:$newVersion")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        db.execSQL("DROP TABLE IF EXISTS clientes")
        onCreate(db)

    }
}