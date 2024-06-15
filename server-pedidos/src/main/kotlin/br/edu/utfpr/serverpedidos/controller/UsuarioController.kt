package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.UsuarioDTO
import br.edu.utfpr.serverpedidos.repository.UsuarioRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository
) {

    @GetMapping
    fun list(): List<UsuarioDTO> {
        val usuarios = usuarioRepository.findAll()
        return usuarios.map { UsuarioDTO.fromEntity(it) }
    }

    @GetMapping("/{email}")
    fun findByEmail(@PathVariable email: String): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioRepository.findByEmail(email).firstOrNull()
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario))
    }

    @PostMapping
    fun save(@Valid @RequestBody usuario: UsuarioDTO): UsuarioDTO {
        val usuarioAtualizado = usuarioRepository.save(usuario.toEntity())
        return UsuarioDTO.fromEntity(usuarioAtualizado)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        usuarioRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
