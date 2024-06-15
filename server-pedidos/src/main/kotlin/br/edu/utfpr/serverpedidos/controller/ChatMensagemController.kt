package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.ChatMensagemDTO
import br.edu.utfpr.serverpedidos.controller.dto.UsuarioDTO
import br.edu.utfpr.serverpedidos.repository.ChatMensagemRepository
import br.edu.utfpr.serverpedidos.repository.ChatRepository
import br.edu.utfpr.serverpedidos.repository.UsuarioRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import kotlin.jvm.optionals.getOrNull

@Controller
class ChatMensagemController(
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val chatRepository: ChatRepository,
    private val chatMensagemRepository: ChatMensagemRepository,
    private val usuarioRepository: UsuarioRepository
) {

    @MessageMapping("/mensagem.privada")
    fun sendPrivateMessage(mensagem: ChatMensagemDTO) {
        if (mensagem.mensagem.isBlank()) {
            return
        }

        val usuario = mensagem.usuario?.id?.let { idUsuario ->
            usuarioRepository.findById(idUsuario).getOrNull()
        } ?: return

        val chatValido = mensagem.chat?.id?.let { idChat ->
            chatRepository.existsById(idChat)
        } ?: false

        if (!chatValido) return

        val mensagemPersistida = chatMensagemRepository.save(mensagem.toEntity())
        val mensagemFormatada = ChatMensagemDTO.fromEntity(mensagemPersistida).copy(
            usuario = UsuarioDTO.fromEntity(usuario)
        )

        this.simpMessagingTemplate.convertAndSend(
            "/queue/chat-${mensagem.chat!!.id}",
            mensagemFormatada
        )
    }
}
