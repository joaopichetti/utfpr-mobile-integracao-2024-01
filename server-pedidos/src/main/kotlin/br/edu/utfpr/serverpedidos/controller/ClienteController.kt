package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.ClienteDTO
import br.edu.utfpr.serverpedidos.repository.ClienteRepository
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/clientes")
class ClienteController(
    private val clienteRepository: ClienteRepository
) {

    @GetMapping
    fun list(): List<ClienteDTO> {
        return clienteRepository.findAll().map {
            ClienteDTO.fromEntity(it)
        }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<ClienteDTO> {
        val cliente = clienteRepository.findById(id).getOrNull()
            ?: return ResponseEntity
                .notFound()
                .build()
        return ResponseEntity.ok(ClienteDTO.fromEntity(cliente))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        clienteRepository.deleteById(id)
        return ResponseEntity
            .noContent()
            .build()
    }

    @PostMapping
    fun save(@Valid @RequestBody cliente: ClienteDTO): ClienteDTO {
        val clienteAtualizado = clienteRepository.save(cliente.toEntity())
        return ClienteDTO.fromEntity(clienteAtualizado)
    }
}
