package com.example.av2pazadailton

import java.io.Serializable



class ClassePessoa : Serializable
{
    var nome: String = ""
    var email: String = ""
    var telefone: String =  ""
    var favorito: Boolean = false

    constructor(nome: String, email: String, telefone: String = "", favorito: Boolean = false){
        this.nome = nome
        this.email = email
        this.telefone = telefone
        this.favorito = favorito
    }
}