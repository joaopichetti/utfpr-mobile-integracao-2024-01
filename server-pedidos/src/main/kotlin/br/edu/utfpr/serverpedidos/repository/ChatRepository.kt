package br.edu.utfpr.serverpedidos.repository

import br.edu.utfpr.serverpedidos.entity.Chat
import br.edu.utfpr.serverpedidos.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Int> {
    fun findByUsuariosContains(usuario: Usuario): List<Chat>
}
