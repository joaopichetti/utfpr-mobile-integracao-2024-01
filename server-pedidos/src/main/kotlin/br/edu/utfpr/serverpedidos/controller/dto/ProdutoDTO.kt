package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Produto
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ProdutoDTO(
    val id: Int,
    @field:NotBlank(message = "{nome.notblank}")
    @field:Size(max = 100, message = "{nome.size}")
    val nome: String = "",
    @field:Positive(message = "{preco.positive}")
    val preco: BigDecimal = BigDecimal.ZERO,
    @field:NotNull(message = "{categoria.notnull}")
    val categoria: CategoriaDTO? = null
) {
    fun toEntity(): Produto = Produto(
        id = this.id,
        nome = this.nome,
        preco = this.preco,
        categoria = this.categoria?.toEntity()
    )

    companion object {
        fun fromEntity(produto: Produto): ProdutoDTO = ProdutoDTO(
            id = produto.id,
            nome = produto.nome,
            preco = produto.preco,
            categoria = produto.categoria?.let { CategoriaDTO.fromEntity(it) }
        )
    }
}
