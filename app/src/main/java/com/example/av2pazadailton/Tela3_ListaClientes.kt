package com.example.av2pazadailton

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tela3_ListaClientes : AppCompatActivity() {

    private val TAG = "Adailton_av2"

    private lateinit var listaClientesView: ListView
    private var arrayListClientes = ArrayList<ClassePessoa>()
    private lateinit var adaptadorPersonalizado: ClasseAdaptadoraDeLinha

    private lateinit var banco: DB
    private lateinit var clienteDao: ClienteDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Tela3_ListaClientes->onCreate()")
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela3_lista_clientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tela3_lista_clientes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DB(baseContext)
        clienteDao = ClienteDAO(banco)

        listaClientesView = findViewById(R.id.t3_lista_personalizada)
        atualizarListaDoBanco()

        listaClientesView.setOnItemClickListener { parent, view, position, id ->
            val clienteSelecionado = arrayListClientes[position]
            Log.d(TAG, "Tela3_ListaClientes->Clique na lista. Cliente: ${clienteSelecionado}")

            val intencaoEditar = Intent(this, Tela4_ClienteEditar::class.java)
            intencaoEditar.putExtra("cliente_objeto", clienteSelecionado)
            intencaoEditar.putExtra("email_chave", clienteSelecionado.email)
            contratoComTelaEditar.launch(intencaoEditar)
        }
    }

    private val contratoComTelaEditar =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult -> Log.d(TAG, "Tela2_ListaClientes->Retorna da Tela4. ResultCode: ${result.resultCode}")
            if(result.resultCode == RESULT_OK){
                atualizarListaDoBanco()
            }
        }

    private fun atualizarListaDoBanco(){
        arrayListClientes = clienteDao.listar()

        adaptadorPersonalizado = ClasseAdaptadoraDeLinha(this, arrayListClientes)
        listaClientesView.adapter = adaptadorPersonalizado
        Log.d(TAG, "Tela3_ListaClientes-> Lista atualizada no visor. Total: ${arrayListClientes.size}")

    }

    fun novo_cliente(v: View){
        Log.d(TAG, "Tela3_ListaClientes->novo() Abrindo formulário vazio na Tela 4.")
        val intencaoNovo = Intent(this, Tela4_ClienteEditar::class.java)
        intencaoNovo.putExtra("email_chave", "")
        contratoComTelaEditar.launch(intencaoNovo)
    }
}