package br.edu.utfpr.apppedidos.data.cep.network

import br.edu.utfpr.apppedidos.data.cep.Cep
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiCepService {
    @GET("ws/{cep}/json")
    suspend fun findByCep(@Path("cep") cep: String): Cep
}