package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "meu_produto")
class Produto(
    @Id
    @GeneratedValue
    var id: Int,
    @Column(name = "nome_produto", nullable = false, length = 100)
    var nome: String = "",
    @Column(name = "valor_venda", nullable = false, precision = 15, scale = 2)
    var preco: BigDecimal = BigDecimal.ZERO,
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_categoria")
    var categoria: Categoria? = null
)
