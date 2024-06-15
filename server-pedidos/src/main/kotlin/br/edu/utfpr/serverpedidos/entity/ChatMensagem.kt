package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.ZonedDateTime

@Entity
class ChatMensagem(
    @Id
    @GeneratedValue
    var id: Int = 0,
    @ManyToOne(optional = false)
    var chat: Chat? = null,
    @ManyToOne(optional = false)
    var usuario: Usuario? = null,
    var mensagem: String = "",
    @Column(updatable = false)
    var dataHora: ZonedDateTime = ZonedDateTime.now()
)
