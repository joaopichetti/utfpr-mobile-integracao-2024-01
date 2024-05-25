package br.edu.utfpr.apppedidos.data.cliente.network

import br.edu.utfpr.apppedidos.data.cliente.Cliente
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "http://10.0.2.2:8080"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface ApiClientesService {
    @GET("clientes")
    suspend fun findAll(): List<Cliente>

    @GET("clientes/{id}")
    suspend fun findById(@Path("id") id: Int): Cliente

    @DELETE("clientes/{id}")
    suspend fun delete(@Path("id") id: Int)

    @POST("clientes")
    suspend fun save(@Body cliente: Cliente): Cliente
}

object ApiClientes {
    val retrofitService: ApiClientesService by lazy {
        retrofit.create(ApiClientesService::class.java)
    }
}