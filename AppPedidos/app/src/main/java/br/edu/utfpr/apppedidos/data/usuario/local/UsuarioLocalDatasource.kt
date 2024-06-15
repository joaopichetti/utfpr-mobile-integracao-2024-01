package br.edu.utfpr.apppedidos.data.usuario.local

import android.content.Context
import br.edu.utfpr.apppedidos.data.usuario.Usuario
import kotlinx.coroutines.flow.first

class UsuarioLocalDatasource(
    private val context: Context
) {
    suspend fun getUsuarioAtual(): Usuario {
        return context.usuarioDataStore.data.first().let {
            Usuario(
                id = it.id,
                nome = it.nome,
                email = it.email
            )
        }
    }

    suspend fun atualizarUsuarioAtual(novoUsuario: Usuario) {
        context.usuarioDataStore.updateData { usuarioAtual ->
            usuarioAtual.toBuilder()
                .setId(novoUsuario.id)
                .setNome(novoUsuario.nome)
                .setEmail(novoUsuario.email)
                .build()
        }
    }
}