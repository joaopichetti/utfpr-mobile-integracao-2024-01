package br.edu.utfpr.apppedidos.data.cliente

data class Endereco(
    val cep: String = "",
    val logradouro: String = "",
    val numero: Int = 0,
    val complemento: String = "",
    val bairro: String = "",
    val cidade: String = ""
)