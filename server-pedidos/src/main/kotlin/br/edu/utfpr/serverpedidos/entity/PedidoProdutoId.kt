package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Embeddable
class PedidoProdutoId(
    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    var pedido: Pedido? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    var produto: Produto? = null
)
