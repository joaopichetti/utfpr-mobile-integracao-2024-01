package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Pedido
import br.edu.utfpr.serverpedidos.entity.StatusPedido
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.time.ZonedDateTime

data class PedidoDTO(
    val id: Int = 0,
    val data: ZonedDateTime? = null,
    val status: StatusPedido = StatusPedido.AGUARDANDO,
    @field:NotNull(message = "{cliente.notnull}")
    val cliente: ClienteDTO? = null,
    @field:NotNull(message = "{produtos.notempty}")
    @field:NotEmpty(message = "{produtos.notempty}")
    @field:Valid
    val produtos: List<PedidoProdutoDTO>? = null
) {
    fun toEntity(): Pedido = Pedido(
        id = this.id,
        status = this.status,
        cliente = this.cliente?.toEntity()
    ).also { pedido ->
        pedido.produtos = produtos?.map { pedidoProdutoDTO ->
            pedidoProdutoDTO.toEntity(pedido)
        } ?: emptyList()
    }

    companion object {
        fun fromEntity(pedido: Pedido): PedidoDTO = PedidoDTO(
            id = pedido.id,
            data = pedido.data,
            status = pedido.status,
            cliente = pedido.cliente?.let { ClienteDTO.fromEntity(it) },
            produtos = pedido.produtos.map { PedidoProdutoDTO.fromEntity(it) }
        )
    }
}
