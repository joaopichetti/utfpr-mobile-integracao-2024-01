package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.ChatMensagem
import java.time.ZonedDateTime

data class ChatMensagemDTO(
    val id: Int = 0,
    val chat: ChatDTO? = null,
    val usuario: UsuarioDTO? = null,
    val mensagem: String = "",
    val dataHora: ZonedDateTime = ZonedDateTime.now()
) {
    fun toEntity(): ChatMensagem = ChatMensagem(
        id = this.id,
        chat = this.chat?.toEntity(),
        usuario = this.usuario?.toEntity(),
        mensagem = this.mensagem,
        dataHora = this.dataHora
    )

    companion object {
        fun fromEntity(chatMensagem: ChatMensagem): ChatMensagemDTO = ChatMensagemDTO(
            id = chatMensagem.id,
            chat = chatMensagem.chat?.let { ChatDTO.fromEntity(it) },
            usuario = chatMensagem.usuario?.let { UsuarioDTO.fromEntity(it) },
            mensagem = chatMensagem.mensagem,
            dataHora = chatMensagem.dataHora
        )
    }
}
