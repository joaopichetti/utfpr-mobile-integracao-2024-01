package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Chat
import java.time.ZonedDateTime

data class ChatDTO(
    val id: Int = 0,
    val dataCriacao: ZonedDateTime = ZonedDateTime.now(),
    val usuarios: List<UsuarioDTO> = emptyList()
) {
    fun toEntity(): Chat = Chat(
        id = this.id,
        dataCriacao = this.dataCriacao,
        usuarios = this.usuarios.map { it.toEntity() }
    )

    companion object {
        fun fromEntity(chat: Chat): ChatDTO = ChatDTO(
            id = chat.id,
            dataCriacao = chat.dataCriacao,
            usuarios = chat.usuarios.map { UsuarioDTO.fromEntity(it) }
        )
    }
}
