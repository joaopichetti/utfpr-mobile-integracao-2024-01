package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Entity
class Categoria(
    @Id
    @GeneratedValue
    var id: Int = 0,
    @field:NotBlank(message = "{nome.notblank}")
    var nome: String = ""
)
