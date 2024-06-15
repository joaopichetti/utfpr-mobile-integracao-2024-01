package br.edu.utfpr.apppedidos.data.usuario.network

import br.edu.utfpr.apppedidos.data.usuario.Usuario
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiUsuarioService {
    @GET("usuarios/{email}")
    suspend fun findByEmail(@Path("email") email: String): Response<Usuario>

    @GET("usuarios")
    suspend fun findAll(): List<Usuario>
}