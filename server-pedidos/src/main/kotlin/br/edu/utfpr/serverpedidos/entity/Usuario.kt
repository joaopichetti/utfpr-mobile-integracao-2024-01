package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Usuario(
    @Id
    @GeneratedValue
    var id: Int = 0,
    var nome: String = "",
    var email: String = ""
)
