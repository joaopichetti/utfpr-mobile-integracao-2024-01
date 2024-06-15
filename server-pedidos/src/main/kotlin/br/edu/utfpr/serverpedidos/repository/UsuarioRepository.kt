package br.edu.utfpr.serverpedidos.repository

import br.edu.utfpr.serverpedidos.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Int> {
    fun findByEmail(email: String): List<Usuario>
}
