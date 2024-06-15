package br.edu.utfpr.apppedidos.ui.chat.list

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.data.chat.Chat
import br.edu.utfpr.apppedidos.data.network.ApiService
import br.edu.utfpr.apppedidos.data.usuario.Usuario
import br.edu.utfpr.apppedidos.data.usuario.local.UsuarioLocalDatasource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatListUiState(
    val usuario: Usuario? = null,
    val isLoadingUser: Boolean = false,
    val isAccessing: Boolean = false,
    val email: String = "",
    @StringRes
    val emailErrorCode: Int? = null,
    val isLoadingChats: Boolean = false,
    val hasErrorLoadingChats: Boolean = false,
    val chats: List<Chat> = emptyList()
) {
    val isSuccess get(): Boolean = usuario != null &&
            !isLoadingUser &&
            !hasErrorLoadingChats &&
            !isLoadingChats
}

class ChatListViewModel(
    private val usuarioDatasource: UsuarioLocalDatasource
) : ViewModel() {
    private val tag: String = this::class.java.simpleName

    var uiState: ChatListUiState by mutableStateOf(ChatListUiState())

    init {
        loadUser()
    }

    private fun loadUser() {
        uiState = uiState.copy(
            isLoadingUser = true
        )
        viewModelScope.launch {
            try {
                delay(2000)
                val usuario = usuarioDatasource.getUsuarioAtual()
                val usuarioValido = usuario.id > 0
                uiState = uiState.copy(
                    isLoadingUser = false,
                    usuario = if (usuarioValido) usuario else null
                )
                if (usuarioValido) {
                    loadChats()
                }
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar usuário do DataStore", ex)
                uiState = uiState.copy(
                    isLoadingUser = false,
                    usuario = null
                )
            }
        }
    }

    fun loadChats() {
        if (uiState.usuario == null) return

        uiState = uiState.copy(
            isLoadingChats = true,
            hasErrorLoadingChats = false
        )
        viewModelScope.launch {
            delay(2000)
            uiState = try {
                uiState.copy(
                    isLoadingChats = false,
                    chats = ApiService.chat.findByUsuarioId(uiState.usuario!!.id)
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar os chats do usuário ${uiState.usuario?.email}", ex)
                uiState.copy(
                    isLoadingChats = false,
                    hasErrorLoadingChats = true
                )
            }
        }
    }

    fun access() {
        uiState = uiState.copy(isAccessing = true)
        viewModelScope.launch {
            try {
                delay(2000)
                val response = ApiService.usuarios.findByEmail(uiState.email)
                if (response.isSuccessful) {
                    val usuario = response.body()
                    usuario?.let {
                        usuarioDatasource.atualizarUsuarioAtual(it)
                    }
                    uiState = uiState.copy(
                        usuario = usuario,
                        isAccessing = false
                    )
                    loadChats()
                } else {
                    val errorCode: Int = when (response.code()) {
                        400 -> R.string.email_invalido
                        404 -> R.string.usuario_nao_encontrado
                        else -> R.string.erro_ao_acessar_o_chat
                    }
                    uiState = uiState.copy(
                        isAccessing = false,
                        emailErrorCode = errorCode
                    )
                }
            } catch(ex: Exception) {
                Log.d(tag, "Erro ao acessar o chat, usando o e-mail ${uiState.email}", ex)
                uiState = uiState.copy(
                    isAccessing = false,
                    emailErrorCode = R.string.erro_ao_acessar_o_chat
                )
            }
        }
    }

    fun onEmailChanged(value: String) {
        if (uiState.email != value) {
            uiState = uiState.copy(
                email = value,
                emailErrorCode = validateEmail(value)
            )
        }
    }

    @StringRes
    private fun validateEmail(email: String): Int? =
        if (email.isBlank() || !email.matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))) {
            R.string.email_invalido
        } else {
            null
        }

    fun logout() {
        uiState = uiState.copy(
            usuario = null,
            isAccessing = false,
            isLoadingChats = false,
            hasErrorLoadingChats = false,
            chats = emptyList()
        )
        viewModelScope.launch {
            try {
                usuarioDatasource.atualizarUsuarioAtual(Usuario())
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao fazer logout", ex)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]
                ChatListViewModel(
                    usuarioDatasource = UsuarioLocalDatasource(context = application!!)
                )
            }
        }
    }
}









