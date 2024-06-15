package br.edu.utfpr.apppedidos.data.chat.network

import br.edu.utfpr.apppedidos.data.chat.Chat
import br.edu.utfpr.apppedidos.data.chat.ChatMensagem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiChatService {
    @GET("chats/{chatId}")
    suspend fun findById(@Path("chatId") chatId: Int): Chat

    @GET("chats")
    suspend fun findByUsuarioId(@Query("userId") usuarioId: Int): List<Chat>

    @GET("chats/{chatId}/mensagens")
    suspend fun findChatMessages(@Path("chatId") chatId: Int): List<ChatMensagem>

    @POST("chats/iniciar")
    suspend fun start(@Body chat: Chat): Chat
}