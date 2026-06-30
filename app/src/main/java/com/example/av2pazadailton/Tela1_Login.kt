package com.example.av2pazadailton

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tela1_Login : AppCompatActivity() {

    private val TAG = "Adailton_av2"

    private lateinit var edEmail: EditText
    private lateinit var edSenha: EditText
    private lateinit var cbPermanecer: CheckBox

    private lateinit var banco: DB
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela1_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tela1_login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DB(baseContext)
        usuarioDao = UsuarioDao(banco)

        edEmail = findViewById(R.id.t1_ed_usuario)
        edSenha = findViewById(R.id.t1_ed_senha)
        cbPermanecer = findViewById(R.id.t1_cb_permanecer)

        val sharedPref = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val conectado = sharedPref.getBoolean("permanecer_conectado", false)

        if(conectado){
            Log.d(TAG, "Tela1_Login->Usuário já marcado para permanecer conectado. Indo direto para Tela3.")
            irParaListaClientes()
        }
    }

    fun  entrar(v: View) {
        val email = edEmail.text.toString().trim()
        val senha = edSenha.text.toString().trim()

        Log.d(TAG, "Tela1_Login->entrar() Tentando logar com e-mail: $email")
        if(email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val sucesso = usuarioDao.validarLogin(email, senha)
        if(sucesso){
            val sharedPref = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                putBoolean("permanecer_conectado", cbPermanecer.isChecked)
                apply()
            }

            Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT). show()
            irParaListaClientes()
        }else{
            Toast.makeText(this, "E-mail ou senha incorretos!", Toast.LENGTH_LONG).show()
        }
    }

    fun ir_para_cadastro_usuario(v: View) {
        Log.d(TAG, "Tela1_Login->cadastrar_usuario() Indo para tela2")
        startActivity(Intent(this, Tela2_CadastroUsuario::class.java))
    }

    private fun irParaListaClientes() {
        startActivity(Intent(this, Tela3_ListaClientes::class.java))
        finish()
    }
}