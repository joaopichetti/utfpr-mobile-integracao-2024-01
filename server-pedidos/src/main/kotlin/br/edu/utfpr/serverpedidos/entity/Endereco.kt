package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

@Entity
class Endereco(
    @Id
    var id: Int = 0,
    var cep: String = "",
    var logradouro: String = "",
    var numero: Int = 0,
    var complemento: String = "",
    var bairro: String = "",
    var cidade: String = "",
    @OneToOne
    @MapsId
    var cliente: Cliente? = null
)
