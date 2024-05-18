package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.PrimaryKeyJoinColumn

@Entity
class Cliente(
    @Id
    @GeneratedValue
    var id: Int = 0,
    var nome: String = "",
    var cpf: String = "",
    var telefone: String = "",
    @OneToOne(mappedBy = "cliente", cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    var endereco: Endereco? = null
)
