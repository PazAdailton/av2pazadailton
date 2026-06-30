package com.example.av2pazadailton

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class UsuarioDao {
    private lateinit var sqlOpen: SQLiteDatabase
    private var banco: DB

    constructor(db: DB){
        banco = db
    }

    fun salvarUsuario(nome: String, email: String, senha: String): Long {
        sqlOpen = banco.writableDatabase
        val content = ContentValues()
        content.put("nome_usuario", nome)
        content.put("email_usuario", email)
        content.put("senha_usuario", senha)

        val id = sqlOpen.insert("usuario", "", content)
        Log.d("Adailton_v2", "UsuárioDAO->salvarUsuario() Cadastrado ID: $id")
        return id
    }

    fun validarLogin(email: String, senha: String): Boolean {
        sqlOpen = banco.readableDatabase
        val colunas = arrayOf("cod_usuario")
        val selection = "email_usuario = ? And senha_usuario = ?"
        val selectionArgs = arrayOf(email, senha)

        val cursor = sqlOpen.query("usuario", colunas, selection, selectionArgs, null, null, null)
        val logado = cursor.count > 0
        cursor.close()

        Log.d("Adailton_v2", "UsuarioDAO->validarLogin() Resultado: $logado")
        return logado
    }
}