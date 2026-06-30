package com.example.av2pazadailton

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tela4_ClienteEditar : AppCompatActivity() {

    private val TAG = "Adailton_av2"
    private lateinit var txTitulo: TextView
    private lateinit var edNome: EditText
    private lateinit var edEmail: EditText
    private lateinit var edTelefone: EditText
    private lateinit var rbFavorito: RadioButton
    private lateinit var btExcluir: Button

    private lateinit var banco: DB
    private lateinit var clienteDao: ClienteDAO
    private var emailChaveOriginal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Tela4_ClienteEditar->onCreate()")
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela4_cliente_editar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tela4_cliente_editar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        banco = DB(baseContext)
        clienteDao = ClienteDAO(banco)

        txTitulo = findViewById(R.id.t4_tx_titulo)
        edNome = findViewById(R.id.t4_ed_nome)
        edEmail = findViewById(R.id.t4_ed_email)
        edTelefone = findViewById(R.id.t4_ed_telefone)
        rbFavorito = findViewById(R.id.t4_rb_favo)
        btExcluir = findViewById(R.id.t4_bt_excluir)

        emailChaveOriginal = intent.getStringExtra("email_chave") ?: ""
        val clienteRecebido = IntentCompat.getSerializableExtra(intent, "cliente_objeto",
            ClassePessoa::class.java)

        if(emailChaveOriginal.isNotEmpty() && clienteRecebido != null) {
            txTitulo.text = "Editar Cliente"
            btExcluir.visibility = View.VISIBLE

            edNome.setText(clienteRecebido.nome)
            edEmail.setText(clienteRecebido.email)
            edTelefone.setText(clienteRecebido.telefone)
            rbFavorito.isChecked = clienteRecebido.favorito
        }else{
            txTitulo.text = "Nova Pessoa"
            btExcluir.visibility = View.INVISIBLE
            edNome.setText("")
            edEmail.setText("")
            edTelefone.setText("")
            rbFavorito.isChecked = false
        }
    }

    private val contratoRecebimentoContato =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->  Log.d(TAG, "Tela4_ClienteEditar->receptor_de_resultados() result: $result")

            if(result.resultCode == 222) {
                val contatoRetorno = result.data?.let {
                    IntentCompat.getSerializableExtra(it, "objeto_de_retorno", ClassePessoa::class.java)
                }
                if(contatoRetorno != null){
                    edNome.setText(contatoRetorno.nome)
                    edEmail.setText(contatoRetorno.email)
                    edTelefone.setText(contatoRetorno.telefone)
                    Log.d(TAG, "Tela4_ClienteEditar->Campos populados com dados vindos da agenda")
                }
            }
        }

    fun importar_do_contato(v: View){
        Log.d(TAG, "Tela4_ClienteEditar->importar_do_contato()")
        val intencaoContatos = Intent(this, Tela5_ListaContatos::class.java)
        intencaoContatos.putExtra("origem_chamada", "tela4")
        contratoRecebimentoContato.launch(intencaoContatos)
    }

    fun salvar(v: View) {
        Log.d(TAG, "Tela4_ClienteEditar->salvar()")

        val nome = edNome.text.toString().trim()
        val email = edEmail.text.toString().trim()
        val telefone = edTelefone.text.toString().trim()
        val favorito = rbFavorito.isChecked

        if(nome.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nome e E-mail são obrigatórios!", Toast.LENGTH_SHORT).show()
            return
        }

        val clienteFormulario = ClassePessoa(nome, email, telefone, favorito)

        if(emailChaveOriginal.isEmpty()){
            clienteDao.inserir(clienteFormulario)
            Toast.makeText(this, "Cliente inserido com sucesso!", Toast.LENGTH_SHORT).show()
        }else{
            clienteDao.atualizar(emailChaveOriginal, clienteFormulario)
            Toast.makeText(this, "Cliente atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        }
        setResult(RESULT_OK)
        finish()
    }

    fun excluir(v: View) {
        Log.d(TAG, "Tela4_ClienteEditar->excluir()")
        if (emailChaveOriginal.isNotEmpty()) {
            clienteDao.excluir(emailChaveOriginal)
            Toast.makeText(this, "Cliente removido com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
        }
        finish()
    }

    fun cancelar(v: View) {
        Log.d(TAG, "Tela4_ClienteEditar->cancelar()")
        finish()
    }
}