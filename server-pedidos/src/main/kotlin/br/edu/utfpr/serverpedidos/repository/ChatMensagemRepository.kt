package br.edu.utfpr.serverpedidos.repository

import br.edu.utfpr.serverpedidos.entity.Chat
import br.edu.utfpr.serverpedidos.entity.ChatMensagem
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMensagemRepository : JpaRepository<ChatMensagem, Int> {
    fun findByChat(chat: Chat): List<ChatMensagem>
}
