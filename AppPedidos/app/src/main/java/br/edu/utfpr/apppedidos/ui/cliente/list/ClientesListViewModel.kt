package br.edu.utfpr.apppedidos.ui.cliente.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.apppedidos.data.cliente.Cliente
import br.edu.utfpr.apppedidos.data.cliente.Endereco
import br.edu.utfpr.apppedidos.data.cliente.network.ApiClientes
import kotlinx.coroutines.launch

data class ClientesListUiState(
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val clientes: List<Cliente> = listOf()
) {
    val success get(): Boolean = !loading && !hasError
}

class ClientesListViewModel : ViewModel() {
    private val tag: String = "ClientesListViewModel"
    var uiState: ClientesListUiState by mutableStateOf(ClientesListUiState())

    init {
        load()
    }

    fun load() {
        uiState = uiState.copy(
            loading = true,
            hasError = false
        )
        viewModelScope.launch {
            uiState = try {
                val clientes = ApiClientes.retrofitService.findAll()
                uiState.copy(
                    clientes = clientes,
                    loading = false
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar lista de clientes", ex)
                uiState.copy(
                    hasError = true,
                    loading = false
                )
            }
        }
    }
}

val clientesFake: List<Cliente> = listOf(
    Cliente(
        id = 1,
        nome = "João",
        cpf = "12345678901",
        telefone = "99999999999",
        endereco = Endereco(
            logradouro = "Rua Teste",
            numero = 550,
            cidade = "Pato Branco - PR"
        )
    ),
    Cliente(
        id = 2,
        nome = "José",
        cpf = "23456789012",
        telefone = "88888888888",
        endereco = Endereco(
            logradouro = "Avenida Brasil",
            numero = 1050,
            cidade = "Curitiba - PR"
        )
    )
)
