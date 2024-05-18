package br.edu.utfpr.serverpedidos.controller

import br.edu.utfpr.serverpedidos.entity.Categoria
import br.edu.utfpr.serverpedidos.repository.CategoriaRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/categorias")
class CategoriaController(
    private val categoriaRepository: CategoriaRepository
) {

    @GetMapping
    fun list(@RequestParam nome: String?): List<Categoria> {
        nome?.let {
            return categoriaRepository.findByNomeContainsIgnoreCase(it)
        }
        return categoriaRepository.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<Categoria> {
        val categoria = categoriaRepository.findById(id).getOrNull()
            ?: return ResponseEntity
                .notFound()
                .build()
        return ResponseEntity.ok(categoria)
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
    fun save(@Valid @RequestBody categoria: Categoria): Categoria {
        return categoriaRepository.save(categoria)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: ""
            errors[fieldName] = errorMessage
        }
        return errors
    }
}
