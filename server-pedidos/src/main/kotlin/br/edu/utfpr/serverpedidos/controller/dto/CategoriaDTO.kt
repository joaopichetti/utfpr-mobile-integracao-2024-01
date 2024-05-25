package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Categoria
import jakarta.validation.constraints.NotBlank

data class CategoriaDTO(
    val id: Int = 0,
    @field:NotBlank(message = "{nome.notblank}")
    val nome: String = ""
) {
    fun toEntity(): Categoria = Categoria(
        id = this.id,
        nome = this.nome
    )

    companion object {
        fun fromEntity(categoria: Categoria): CategoriaDTO = CategoriaDTO(
            id = categoria.id,
            nome = categoria.nome
        )
    }
}
