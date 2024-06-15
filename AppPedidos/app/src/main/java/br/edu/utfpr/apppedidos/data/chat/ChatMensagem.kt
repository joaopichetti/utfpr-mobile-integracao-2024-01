package br.edu.utfpr.apppedidos.data.chat

import br.edu.utfpr.apppedidos.data.usuario.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class ChatMensagem(
    val id: Int = 0,
    val chat: Chat = Chat(),
    val usuario: Usuario = Usuario(),
    val mensagem: String = "",
    val dataHora: String = ""
)