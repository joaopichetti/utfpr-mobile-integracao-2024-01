package br.edu.utfpr.apppedidos.ui.chat.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.edu.utfpr.apppedidos.data.chat.Chat
import br.edu.utfpr.apppedidos.data.chat.ChatMensagem
import br.edu.utfpr.apppedidos.data.network.ApiService
import br.edu.utfpr.apppedidos.data.usuario.Usuario
import br.edu.utfpr.apppedidos.data.usuario.local.UsuarioLocalDatasource
import br.edu.utfpr.apppedidos.ui.Arguments
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient

data class ChatUiState(
    val usuario: Usuario = Usuario(),
    val chat: Chat = Chat(),
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val mensagens: List<ChatMensagem> = emptyList(),
    val mensagem: String = "",
    val isSending: Boolean = false,
    val hasErrorSending: Boolean = false
) {
    val isSuccess get(): Boolean = !isLoading && !hasErrorLoading
}

class ChatViewModel(
    savedStateHandle: SavedStateHandle,
    private val usuarioDatasource: UsuarioLocalDatasource
) : ViewModel() {
    private val tag: String = this::class.java.simpleName
    private val idChat: Int = savedStateHandle.get<Int>(Arguments.CHAT_ID) ?: 0
    private lateinit var stompSession: StompSessionWithKxSerialization
    private lateinit var collectorJob: Job

    var uiState: ChatUiState by mutableStateOf(ChatUiState())

    init {
        load()
        viewModelScope.launch { initStompSession() }
    }

    fun load() {
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false
        )
        viewModelScope.launch {
            delay(2000)
            try {
                if (uiState.usuario.id <= 0) {
                    uiState = uiState.copy(
                        usuario = usuarioDatasource.getUsuarioAtual()
                    )
                }
                if (uiState.chat.id <= 0) {
                    uiState = uiState.copy(
                        chat = ApiService.chat.findById(idChat)
                    )
                }
                uiState = uiState.copy(
                    mensagens = ApiService.chat.findChatMessages(idChat).asReversed(),
                    isLoading = false
                )
                subscribe()
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar os dados da tela", ex)
                uiState = uiState.copy(
                    isLoading = false,
                    hasErrorLoading = false
                )
            }
        }
    }

    private suspend fun initStompSession() {
        val httpClient = HttpClient(CIO) {
            install(WebSockets)
        }
        val webSocketClient = KtorWebSocketClient(httpClient)
        stompSession = StompClient(webSocketClient)
            .connect("ws://10.0.2.2:8080/chat")
            .withJsonConversions(Json {
                ignoreUnknownKeys = true
            })
    }

    private suspend fun subscribe() {
        val subscription: Flow<ChatMensagem> = stompSession.subscribe(
            "/queue/chat-${idChat}",
            ChatMensagem.serializer()
        )
        collectorJob = viewModelScope.launch {
            subscription.collect { mensagem ->
                onMensagemReceived(mensagem)
            }
        }
    }

    private fun onMensagemReceived(mensagem: ChatMensagem) {
        uiState = uiState.copy(
            mensagens = listOf(
                mensagem,
                *uiState.mensagens.toTypedArray()
            )
        )
    }

    fun onMensagemChanged(value: String) {
        if (uiState.mensagem != value) {
            uiState = uiState.copy(
                mensagem = value
            )
        }
    }

    fun sendMensagem() {
        if (uiState.mensagem.isBlank()) return

        uiState = uiState.copy(
            isSending = true,
            hasErrorSending = false
        )
        viewModelScope.launch {
            delay(2000)
            val mensagem = ChatMensagem(
                chat = uiState.chat,
                usuario = uiState.usuario,
                mensagem = uiState.mensagem
            )
            stompSession.convertAndSend(
                "/app/mensagem.privada",
                mensagem,
                ChatMensagem.serializer()
            )
            uiState = uiState.copy(
                isSending = false,
                mensagem = ""
            )
        }
    }

    override fun onCleared() {
        Log.i(tag, "On Cleared")
        collectorJob.cancel()
        viewModelScope.launch { stompSession.disconnect() }
        super.onCleared()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]
                val savedStateHandle = this.createSavedStateHandle()
                ChatViewModel(
                    savedStateHandle = savedStateHandle,
                    usuarioDatasource = UsuarioLocalDatasource(application!!)
                )
            }
        }
    }
}
















