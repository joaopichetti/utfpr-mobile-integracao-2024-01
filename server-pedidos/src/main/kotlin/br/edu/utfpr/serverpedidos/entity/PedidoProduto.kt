package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import java.math.BigDecimal

@Entity
class PedidoProduto(
    @EmbeddedId
    var id: PedidoProdutoId,
    var valorUnitario: BigDecimal = BigDecimal.ZERO,
    var quantidade: BigDecimal = BigDecimal.ZERO
)
