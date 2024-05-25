package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.ZonedDateTime

@Entity
class Pedido(
    @Id
    @GeneratedValue
    var id: Int = 0,
    @Column(updatable = false)
    var data: ZonedDateTime = ZonedDateTime.now(),
    @Enumerated(value = EnumType.STRING)
    var status: StatusPedido = StatusPedido.AGUARDANDO,
    @ManyToOne(optional = false)
    var cliente: Cliente? = null,
    @OneToMany(mappedBy = "id.pedido", cascade = [CascadeType.ALL], orphanRemoval = true)
    var produtos: List<PedidoProduto> = listOf()
)

enum class StatusPedido {
    AGUARDANDO,
    FATURADO,
    ENVIADO,
    ENTREGUE
}
