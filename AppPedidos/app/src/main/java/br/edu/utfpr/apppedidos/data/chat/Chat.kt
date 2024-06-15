package br.edu.utfpr.apppedidos.data.chat

import br.edu.utfpr.apppedidos.data.usuario.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Int = 0,
    val usuarios: List<Usuario> = listOf()
) {
    fun getNome(usuarioAtual: Usuario): String = usuarios.firstOrNull {
        it.id != usuarioAtual.id
    }?.nome ?: ""
}