package com.example.av2pazadailton

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.CopyOnWriteArrayList

class Tela5_ListaContatos : AppCompatActivity() {

    private val TAG = "Adailton_av2"
    private lateinit var listaContatosView: ListView
    private var listaContatosNomes = ArrayList<String>()
    private var listaObjetoPessoas = CopyOnWriteArrayList<ClassePessoa>()
    private var origem: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Tela5_ListaContatos->onCreate()")
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela5_lista_contatos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tela5_lista_contatos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listaContatosView = findViewById(R.id.t5_lista_contatos)

        origem = intent.getStringExtra("origem_chamada")?: ""
        Log.d(TAG, "Tela5_ListaContatos->Origem da chamada: $origem")

        lerContatosDispositivo()

        val adapatadorSimples = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaContatosNomes)
        listaContatosView.adapter = adapatadorSimples

        listaContatosView.setOnItemClickListener {parent, view, position, id ->
            val contatoSelecionado = listaObjetoPessoas[position]
            Log.d(TAG, "Tela5_ListaContatos->Contato clicado: ${contatoSelecionado.nome}")

            val intentRetorno = Intent()
            intentRetorno.putExtra("objeto_de_retorno", contatoSelecionado)

            setResult(222, intentRetorno)
            finish()
        }
    }

        private fun lerContatosDispositivo() {
            val cr = contentResolver

            val cursor = cr.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null
            )

            if(cursor != null){
                while (cursor.moveToNext()) {
                    val contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val nome = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                    var email = "sem@email.com"
                    var telefone = ""

                    val phones = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
                    )
                    if(phones != null ) {
                        if (phones.moveToNext()) {
                            telefone = phones.getString(phones.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                        }
                        phones.close()
                    }

                    val emails = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                        null, null
                    )
                    if(emails != null) {
                        if(emails.moveToNext()) {
                            email = emails.getString(emails.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))
                        }
                        emails.close()
                    }

                    listaContatosNomes.add("$nome\n$email")
                    listaObjetoPessoas.add(ClassePessoa(nome, email, telefone, false))
                }
                cursor.close()
            }
            Log.d(TAG, "Tela5_ListaContatos->Leitura finalizada. Contatos carregados: ${listaContatosNomes.size}")

    }
}