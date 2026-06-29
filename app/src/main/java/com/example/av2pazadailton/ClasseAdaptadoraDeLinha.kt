package com.example.av2pazadailton

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ClasseAdaptadoraDeLinha(private val context: Activity,
                              private val array_pessoas: ArrayList<ClassePessoa>)

    : ArrayAdapter<ClassePessoa> (context, R.layout.activity_tela3_lista_clientes, array_pessoas)
{
    private val TAG = "Adailton_av2"

    override fun getView (position: Int, view : View?, parent : ViewGroup ) : View {
        Log.d(TAG, "ClasseAdaptadoraDeLinha()->getView() = $position ${array_pessoas.get(position).nome}")
        val inflater = context.layoutInflater
        var pessoa: ClassePessoa = array_pessoas.get(position)

        var linha = inflater.inflate(R.layout.activity_t3_template_para_linha, null, true)

        val image : ImageView = linha.findViewById(R.id.t3_img_favo)
        val tx_nome : TextView  = linha.findViewById( R.id.t3_tx_nome  )
        val tx_email: TextView  = linha.findViewById( R.id.t3_tx_email )
        val tx_telefone: TextView  = linha.findViewById( R.id.t3_tx_telef )

        tx_nome.text = pessoa.nome
        tx_email.text = pessoa.email
        tx_telefone.text = pessoa.telefone

        if(pessoa.favorito) {
            image.setImageResource(android.R.drawable.radiobutton_on_background)
        }else{
            image.setImageResource( android.R.drawable.radiobutton_off_background)
        }
        return linha
    }


}