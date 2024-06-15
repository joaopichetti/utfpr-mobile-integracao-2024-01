package br.edu.utfpr.apppedidos.ui.chat.newchat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.edu.utfpr.apppedidos.data.chat.Chat
import br.edu.utfpr.apppedidos.data.network.ApiService
import br.edu.utfpr.apppedidos.data.usuario.Usuario
import br.edu.utfpr.apppedidos.data.usuario.local.UsuarioLocalDatasource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class NewChatUiState(
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val isStartingChat: Boolean = false,
    val hasErrorStartingChat: Boolean = false,
    val startedChat: Chat? = null
)

class NewChatViewModel(
    private val usuarioDatasource: UsuarioLocalDatasource
) : ViewModel() {
    private val tag = this::class.java.simpleName
    private var usuarioAtual: Usuario? = null
    var uiState: NewChatUiState by mutableStateOf(NewChatUiState())

    init {
        load()
    }

    fun load() {
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false
        )
        viewModelScope.launch {
            delay(2000)
            uiState = try {
                if (usuarioAtual == null) {
                    usuarioAtual = usuarioDatasource.getUsuarioAtual()
                }
                val usuarios = ApiService.usuarios.findAll()
                val usuariosFiltrados = usuarios.filter { it.id != usuarioAtual!!.id }
                uiState.copy(
                    isLoading = false,
                    usuarios = usuariosFiltrados
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar usuários", ex)
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun onUsuarioSelected(usuarioSelecionado: Usuario) {
        uiState = uiState.copy(
            isStartingChat = true,
            hasErrorStartingChat = false
        )
        viewModelScope.launch {
            delay(2000)
            uiState = try {
                val chat = Chat(
                    usuarios = listOf(
                        usuarioAtual!!,
                        usuarioSelecionado
                    )
                )
                val startedChat = ApiService.chat.start(chat)
                uiState.copy(
                    isStartingChat = false,
                    startedChat = startedChat
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao iniciar o chat", ex)
                uiState.copy(
                    isStartingChat = false,
                    hasErrorStartingChat = true
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]
                NewChatViewModel(
                    usuarioDatasource = UsuarioLocalDatasource(context = application!!)
                )
            }
        }
    }
}















