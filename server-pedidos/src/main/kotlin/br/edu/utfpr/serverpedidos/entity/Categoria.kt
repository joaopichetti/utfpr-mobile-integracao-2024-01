package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Categoria(
    @Id
    @GeneratedValue
    var id: Int = 0,
    var nome: String = ""
)
