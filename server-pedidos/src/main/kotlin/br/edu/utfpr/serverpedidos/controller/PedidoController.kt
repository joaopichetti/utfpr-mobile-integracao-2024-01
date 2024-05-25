package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.PedidoDTO
import br.edu.utfpr.serverpedidos.repository.PedidoRepository
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
@RequestMapping("/pedidos")
class PedidoController(
    private val pedidoRepository: PedidoRepository
) {

    @GetMapping
    fun listar(): List<PedidoDTO> {
        return pedidoRepository.findAll().map { PedidoDTO.fromEntity(it) }
    }

    @GetMapping("/page")
    fun paginar(pageable: Pageable): Page<PedidoDTO> {
        return pedidoRepository.findAll(pageable).map { PedidoDTO.fromEntity(it) }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<PedidoDTO> {
        val pedido = pedidoRepository.findById(id).getOrNull()
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(PedidoDTO.fromEntity(pedido))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        pedidoRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping
    fun salvar(@Valid @RequestBody pedido: PedidoDTO): PedidoDTO {
        val pedidoAtualizado = pedidoRepository.save(pedido.toEntity())
        return PedidoDTO.fromEntity(pedidoAtualizado)
    }
}
