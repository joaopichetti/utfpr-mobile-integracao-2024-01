package br.edu.utfpr.apppedidos.data.cliente

data class Cliente(
    val id: Int = 0,
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val endereco: Endereco = Endereco()
)