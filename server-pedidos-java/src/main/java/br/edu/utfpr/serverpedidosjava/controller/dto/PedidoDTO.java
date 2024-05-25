package br.edu.utfpr.serverpedidosjava.controller.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.edu.utfpr.serverpedidosjava.entity.Pedido;
import br.edu.utfpr.serverpedidosjava.entity.Pedido.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PedidoDTO(
        Integer id,
        ZonedDateTime data,
        StatusPedido status,
        @NotNull(message = "{cliente.notnull}") ClienteDTO cliente,
        @NotNull(message = "{produtos.notempty}") @NotEmpty(message = "{produtos.notempty}") @Valid List<PedidoProdutoDTO> produtos) {

    public Pedido toEntity() {
        Pedido pedido = new Pedido();
        pedido.setId(this.id);
        pedido.setData(this.data);
        pedido.setStatus(this.status);
        pedido.setCliente(this.cliente.toEntity());
        pedido.setProdutos(this.produtos.stream().map((pedidoProduto) -> pedidoProduto.toEntity(pedido)).toList());
        return pedido;
    }

    public static PedidoDTO fromEntity(Pedido pedido) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getData(),
                pedido.getStatus(),
                ClienteDTO.fromEntity(pedido.getCliente()),
                pedido.getProdutos().stream().map((pedidoProduto) -> PedidoProdutoDTO.fromEntity(pedidoProduto))
                        .toList());
    }
}
