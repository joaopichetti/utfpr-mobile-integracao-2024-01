package br.edu.utfpr.serverpedidos.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import java.time.ZonedDateTime

@Entity
class Chat(
    @Id
    @GeneratedValue
    var id: Int = 0,
    @Column(updatable = false)
    var dataCriacao: ZonedDateTime = ZonedDateTime.now(),
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_usuario",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "usuario_id")])
    var usuarios: List<Usuario> = listOf()
)
