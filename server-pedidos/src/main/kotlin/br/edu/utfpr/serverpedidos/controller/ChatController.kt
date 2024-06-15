package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.ChatDTO
import br.edu.utfpr.serverpedidos.controller.dto.ChatMensagemDTO
import br.edu.utfpr.serverpedidos.entity.Chat
import br.edu.utfpr.serverpedidos.repository.ChatMensagemRepository
import br.edu.utfpr.serverpedidos.repository.ChatRepository
import br.edu.utfpr.serverpedidos.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/chats")
class ChatController(
    private val chatRepository: ChatRepository,
    private val chatMensagemRepository: ChatMensagemRepository,
    private val usuarioRepository: UsuarioRepository
) {

    @GetMapping
    fun list(@RequestParam userId: Int?): List<ChatDTO> {
        val chats: List<Chat> = if (userId != null) {
            val usuario = usuarioRepository.findById(userId).getOrNull()
                ?: return emptyList()
            chatRepository.findByUsuariosContains(usuario)
        } else {
            chatRepository.findAll()
        }
        return chats.map { ChatDTO.fromEntity(it) }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<ChatDTO> {
        val chat = chatRepository.findById(id).getOrNull()
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(ChatDTO.fromEntity(chat))
    }

    @GetMapping("/{chatId}/mensagens")
    fun listMensagens(@PathVariable chatId: Int): ResponseEntity<List<ChatMensagemDTO>> {
        val chat = chatRepository.findById(chatId).getOrNull()
            ?: return ResponseEntity.notFound().build()

        val mensagens = chatMensagemRepository.findByChat(chat).map { ChatMensagemDTO.fromEntity(it) }
        return ResponseEntity.ok(mensagens)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        chatRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/iniciar")
    fun startChat(@RequestBody chat: ChatDTO): ResponseEntity<ChatDTO> {
        if (chat.usuarios.size < 2) {
            return ResponseEntity.badRequest().build()
        }
        val chatEntity = chat.toEntity()
        val chatsPrimeiroUsuario = chatRepository.findByUsuariosContains(chatEntity.usuarios.first())
        var chatIniciado: Chat? = null
        if (chatsPrimeiroUsuario.isNotEmpty()) {
            val chatsEmComum = chatsPrimeiroUsuario.filter {
                it.usuarios.any { usuario ->
                    usuario.id == chatEntity.usuarios.last().id
                }
            }
            if (chatsEmComum.isNotEmpty()) {
                chatIniciado = chatsEmComum.first()
            }
        }
        if (chatIniciado == null) {
            chatIniciado = chatRepository.save(chatEntity)
        }
        return ResponseEntity.ok(ChatDTO.fromEntity(chatIniciado))
    }
}
