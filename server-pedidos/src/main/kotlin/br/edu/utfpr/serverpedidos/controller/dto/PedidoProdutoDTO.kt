package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Pedido
import br.edu.utfpr.serverpedidos.entity.PedidoProduto
import br.edu.utfpr.serverpedidos.entity.PedidoProdutoId
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class PedidoProdutoDTO(
    @field:NotNull(message = "{produto.notnull}")
    val produto: ProdutoDTO? = null,
    @field:Positive(message = "{valorunitario.positive}")
    val valorUnitario: BigDecimal = BigDecimal.ZERO,
    @field:Positive(message = "{quantidade.positive}")
    val quantidade: BigDecimal = BigDecimal.ZERO
) {
    fun toEntity(pedido: Pedido): PedidoProduto = PedidoProduto(
        id = PedidoProdutoId(
            pedido = pedido,
            produto = this.produto?.toEntity()
        ),
        valorUnitario = this.valorUnitario,
        quantidade = this.quantidade
    )

    companion object {
        fun fromEntity(pedidoProduto: PedidoProduto): PedidoProdutoDTO = PedidoProdutoDTO(
            produto = pedidoProduto.id.produto?.let { ProdutoDTO.fromEntity(it) },
            valorUnitario = pedidoProduto.valorUnitario,
            quantidade = pedidoProduto.quantidade
        )
    }
}
