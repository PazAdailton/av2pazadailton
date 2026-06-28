package com.example.av2pazadailton

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ClienteDAO {
    private lateinit var sqlOpen: SQLiteDatabase
    private var banco: DB

    constructor(db: DB){
        banco = db
    }

    fun inserir(cliente: ClassePessoa): Long {
        sqlOpen = banco.readableDatabase
        val content = ContentValues()
        content.put("nome_cliente", cliente.nome)
        content.put("email_cliente", cliente.email)
        content.put("telefone_cliente", cliente.telefone)
        content.put("favorito_cliente",if (cliente.favorito) 1 else 0)

        val id = sqlOpen.insert("clientes", "", content)
        Log.d("Adailton_v2", "ClienteDAO->inserir() Cliente ID: $id")
        return id

    }

    fun listar(): ArrayList<ClassePessoa> {
        val lista = ArrayList<ClassePessoa>()
        sqlOpen = banco.readableDatabase

        val cursor = sqlOpen.query("clientes", null, null, null, null, null, "cliente_nome ASC")

        while(cursor.moveToNext()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome_cliente"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email_cliente"))
            val telefone = cursor.getString(cursor.getColumnIndexOrThrow("telefone_cliente"))
            val favoritoInt = cursor.getInt(cursor.getColumnIndexOrThrow("favorito_cliente"))

            lista.add(ClassePessoa(nome, email, telefone, favoritoInt == 1))
        }
        cursor.close()
        Log.d("Adailton_av2", "ClienteDAO->listar() Total carregado: ${lista.size}")
        return lista
    }

    fun atualizar(emailAntigo: String, cliente: ClassePessoa): Int {
        sqlOpen = banco.writableDatabase
        val content = ContentValues()
        content.put("nome_cliente", cliente.nome)
        content.put("email_cliente", cliente.email)
        content.put("telefone_cliente", cliente.telefone)
        content.put("favorito_cliente", if (cliente.favorito) 1 else 0)

        val qtd = sqlOpen.update("clientes", content, "email_cliente = ?", arrayOf(emailAntigo))
        Log.d("Adailton_av2", "ClienteDAO->arualizar() Linhas afetadas: $qtd")
        return qtd
    }

    fun excluir(email: String): Int {
        sqlOpen = banco.writableDatabase
        val qtd = sqlOpen.delete("clientes", "email_cliente = ?", arrayOf(email))
        Log.d("Adailton_av2", "ClienteDAO->excluir() Linhas removidas: $qtd")
        return qtd
    }

}