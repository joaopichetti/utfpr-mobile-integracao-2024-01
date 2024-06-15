package br.edu.utfpr.apppedidos.data.usuario

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int = 0,
    val nome: String = "",
    val email: String = ""
)