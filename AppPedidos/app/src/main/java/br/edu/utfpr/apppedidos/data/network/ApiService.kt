package br.edu.utfpr.apppedidos.data.network

import br.edu.utfpr.apppedidos.data.cep.network.ApiCepService
import br.edu.utfpr.apppedidos.data.chat.network.ApiChatService
import br.edu.utfpr.apppedidos.data.cliente.network.ApiClientesService
import br.edu.utfpr.apppedidos.data.usuario.network.ApiUsuarioService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private val json = Json { ignoreUnknownKeys = true }
private val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

private const val API_PEDIDOS_BASE_URL = "http://10.0.2.2:8080"
private val apiPedidosClient = Retrofit.Builder()
    .addConverterFactory(jsonConverterFactory)
    .baseUrl(API_PEDIDOS_BASE_URL)
    .build()

private const val API_CEP_BASE_URL = "https://viacep.com.br"
private val apiCepClient = Retrofit.Builder()
    .addConverterFactory(jsonConverterFactory)
    .baseUrl(API_CEP_BASE_URL)
    .build()

object ApiService {
    val clientes: ApiClientesService by lazy {
        apiPedidosClient.create(ApiClientesService::class.java)
    }
    val cep: ApiCepService by lazy {
        apiCepClient.create(ApiCepService::class.java)
    }
    val usuarios: ApiUsuarioService by lazy {
        apiPedidosClient.create(ApiUsuarioService::class.java)
    }
    val chat: ApiChatService by lazy {
        apiPedidosClient.create(ApiChatService::class.java)
    }
}
