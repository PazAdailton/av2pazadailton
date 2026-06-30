package com.example.av2pazadailton

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tela2_CadastroUsuario : AppCompatActivity() {

    private val TAG = "Adailton_av2"

    private lateinit var edNome: EditText
    private lateinit var edEmail: EditText
    private lateinit var edSenha: EditText

    private lateinit var banco: DB
    private lateinit var usuarioDAO: UsuarioDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Tela2_CadastroUsuario->onCreate()")
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela2_cadastro_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tela2_cadastro_usuario)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DB(baseContext)
        usuarioDAO = UsuarioDao(banco)

        edNome = findViewById(R.id.t2_ed_nome)
        edEmail = findViewById(R.id.t2_ed_email)
        edSenha = findViewById(R.id.t2_ed_senha)

    }

    val contrato_de_recebimento_do_contato = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result: ActivityResult -> Log.d(TAG, "Tela2_CadastroUsuario->receptor_de_resultados() result: $result")

        if(result.resultCode == 222){
            Log.d(TAG, "Tela2_CadastroUsuario -> Recebendo objeto de contato serializado")
            val contatoRetorno = result.data?.let {
                IntentCompat.getSerializableExtra(it, "objeto_de_retorno", ClassePessoa::class.java)
            }
        if(contatoRetorno != null){
            edNome.setText(contatoRetorno.nome)
            edEmail.setText(contatoRetorno.email)
            Log.d(TAG, "Tela2_CadastroUsuario -> Campos preenchidos com: ${contatoRetorno.nome}")
            }
        }
    }

    fun buscar_em_contatos(v: View){
        Log.d(TAG, "Tela2_CadastroUsuario->buscar_em_contatos()")
        val intencaoIdaContatos = Intent(this@Tela2_CadastroUsuario, Tela5_ListaContatos::class.java)
        intencaoIdaContatos.putExtra("origem_chamada", "tela2")
        contrato_de_recebimento_do_contato.launch(intencaoIdaContatos)
    }

    fun salvar_usuario(v: View){
        val  nome = edNome.text.toString().trim()
        val email = edEmail.text.toString().trim()
        val senha = edSenha.text.toString().trim()

        Log.d(TAG, "Tela2_CadastroUsuario->salvar_usuario()")

        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }
        val idInserido = usuarioDAO.salvarUsuario(nome, email, senha)
        if(idInserido > 0){
            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(this, "Erro ao gravar usuário no banco de dados.", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelar(v: View){
        Log.d(TAG,"Tela2_CadastroUsuario->cancelar()")
        finish()
    }

}