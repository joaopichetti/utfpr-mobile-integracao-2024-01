package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.ProdutoDTO
import br.edu.utfpr.serverpedidos.repository.ProdutoRepository
import jakarta.validation.Valid
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
@RequestMapping("/produtos")
class ProdutoController(
    private val produtoRepository: ProdutoRepository
) {

    @GetMapping
    fun list(@RequestParam query: String?): List<ProdutoDTO> {
        val produtos = query?.let {
            produtoRepository.findByQuery(it)
        } ?: produtoRepository.findAll()
        return produtos.map { ProdutoDTO.fromEntity(it) }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable(name = "id") codigo: Int): ResponseEntity<ProdutoDTO> {
        val produto = produtoRepository.findById(codigo).getOrNull()
            ?: return ResponseEntity
                .notFound()
                .build()
        return ResponseEntity.ok(ProdutoDTO.fromEntity(produto))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        produtoRepository.deleteById(id)
        return ResponseEntity
            .noContent()
            .build()
    }

    @PostMapping
    fun save(@Valid @RequestBody produto: ProdutoDTO): ProdutoDTO {
        val produtoAtualizado = produtoRepository.save(produto.toEntity())
        return ProdutoDTO.fromEntity(produtoAtualizado)
    }
}
