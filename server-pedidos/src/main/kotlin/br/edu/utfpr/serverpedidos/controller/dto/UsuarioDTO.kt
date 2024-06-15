package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Usuario

data class UsuarioDTO(
    val id: Int = 0,
    val nome: String = "",
    val email: String = ""
) {
    fun toEntity(): Usuario = Usuario(
        id = this.id,
        nome = this.nome,
        email = this.email
    )

    companion object {
        fun fromEntity(usuario: Usuario): UsuarioDTO = UsuarioDTO(
            id = usuario.id,
            nome = usuario.nome,
            email = usuario.email
        )
    }
}
