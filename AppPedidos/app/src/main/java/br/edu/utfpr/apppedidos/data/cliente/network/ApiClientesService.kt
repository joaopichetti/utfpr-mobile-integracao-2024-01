package br.edu.utfpr.apppedidos.data.cliente.network

import br.edu.utfpr.apppedidos.data.cliente.Cliente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiClientesService {
    @GET("clientes")
    suspend fun findAll(): List<Cliente>

    @GET("clientes/{id}")
    suspend fun findById(@Path("id") id: Int): Cliente

    @DELETE("clientes/{id}")
    suspend fun delete(@Path("id") id: Int)

    @POST("clientes")
    suspend fun save(@Body cliente: Cliente): Response<Cliente>
}
