package br.edu.utfpr.apppedidos.ui.cliente.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.apppedidos.data.cliente.Cliente
import br.edu.utfpr.apppedidos.data.cliente.Endereco
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ClientesListUiState(
    val loading: Boolean = false,
    val hasError: Boolean = false,
    val clientes: List<Cliente> = listOf()
) {
    val success get(): Boolean = !loading && !hasError
}

class ClientesListViewModel : ViewModel() {
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
            delay(2000)
            val hasErrorLoading = Random.nextBoolean()
            uiState = if (hasErrorLoading) {
                uiState.copy(
                    hasError = true,
                    loading = false
                )
            } else {
                uiState.copy(
                    clientes = clientesFake,
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
