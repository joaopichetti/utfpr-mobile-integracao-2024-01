package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.controller.dto.CategoriaDTO
import br.edu.utfpr.serverpedidos.repository.CategoriaRepository
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/categorias")
class CategoriaController(
    private val categoriaRepository: CategoriaRepository
) {

    @GetMapping
    fun list(@RequestParam nome: String?): List<CategoriaDTO> {
        val categorias = nome?.let {
            categoriaRepository.findByNomeContainsIgnoreCase(it)
        } ?: categoriaRepository.findAll()
        return categorias.map { CategoriaDTO.fromEntity(it) }
    }

    @GetMapping("/page")
    fun paginar(@RequestParam nome: String?, pageable: Pageable): Page<CategoriaDTO> {
        val categorias = nome?.let {
            categoriaRepository.findByNomeContainsIgnoreCase(it, pageable)
        } ?: categoriaRepository.findAll(pageable)
        return categorias.map { CategoriaDTO.fromEntity(it) }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<CategoriaDTO> {
        val categoria = categoriaRepository.findById(id).getOrNull()
            ?: return ResponseEntity
                .notFound()
                .build()
        return ResponseEntity.ok(CategoriaDTO.fromEntity(categoria))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        categoriaRepository.findById(id).getOrNull()?.let {
            categoriaRepository.delete(it)
        }
        return ResponseEntity
            .noContent()
            .build()
    }

    @PostMapping
    fun save(@Valid @RequestBody categoria: CategoriaDTO): CategoriaDTO {
        val categoriaAtualizada = categoriaRepository.save(categoria.toEntity())
        return CategoriaDTO.fromEntity(categoriaAtualizada)
    }
}
