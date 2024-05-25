package br.edu.utfpr.serverpedidosjava.controller.dto;

import java.math.BigDecimal;

import br.edu.utfpr.serverpedidosjava.entity.Pedido;
import br.edu.utfpr.serverpedidosjava.entity.PedidoProduto;
import br.edu.utfpr.serverpedidosjava.entity.PedidoProdutoId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoProdutoDTO(
        @NotNull(message = "{produto.notnull}") ProdutoDTO produto,
        @NotNull(message = "{valorunitario.notnull}") @Positive(message = "{valorunitario.positive}") BigDecimal valorUnitario,
        @NotNull(message = "{quantidade.notnull}") @Positive(message = "{quantidade.positive}") BigDecimal quantidade) {

    public PedidoProduto toEntity(Pedido pedido) {
        PedidoProdutoId pedidoProdutoId = new PedidoProdutoId();
        pedidoProdutoId.setPedido(pedido);
        pedidoProdutoId.setProduto(this.produto.toEntity());

        PedidoProduto pedidoProduto = new PedidoProduto();
        pedidoProduto.setId(pedidoProdutoId);
        pedidoProduto.setValorUnitario(this.valorUnitario);
        pedidoProduto.setQuantidade(this.quantidade);

        return pedidoProduto;
    }

    public static PedidoProdutoDTO fromEntity(PedidoProduto pedidoProduto) {
        return new PedidoProdutoDTO(
                ProdutoDTO.fromEntity(pedidoProduto.getId().getProduto()),
                pedidoProduto.getValorUnitario(),
                pedidoProduto.getQuantidade());
    }
}
