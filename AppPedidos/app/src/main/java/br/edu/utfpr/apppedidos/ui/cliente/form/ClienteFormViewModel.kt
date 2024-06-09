package br.edu.utfpr.apppedidos.ui.cliente.form

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.data.cliente.Cliente
import br.edu.utfpr.apppedidos.data.cliente.Endereco
import br.edu.utfpr.apppedidos.data.network.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

data class FormField(
    val value: String = "",
    @StringRes
    val errorMessageCode: Int? = null
)

data class FormState(
    val nome: FormField = FormField(),
    val cpf: FormField = FormField(),
    val telefone: FormField = FormField(),
    val cep: FormField = FormField(),
    val logradouro: FormField = FormField(),
    val numero: FormField = FormField(),
    val complemento: FormField = FormField(),
    val bairro: FormField = FormField(),
    val cidade: FormField = FormField(),
    val isSearchingCep: Boolean = false
) {
    val isValid get(): Boolean = nome.errorMessageCode == null &&
            cpf.errorMessageCode == null &&
            telefone.errorMessageCode == null &&
            cep.errorMessageCode == null &&
            logradouro.errorMessageCode == null &&
            numero.errorMessageCode == null &&
            complemento.errorMessageCode == null &&
            bairro.errorMessageCode == null &&
            cidade.errorMessageCode == null
}

data class ClienteFormUiState(
    val clienteId: Int = 0,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val formState: FormState = FormState(),
    val isSaving: Boolean = false,
    val hasErrorSaving: Boolean = false,
    val clienteSaved: Boolean = false,
    val apiValidationError: String = ""
) {
    val isNewCliente get(): Boolean = clienteId <= 0
    val isSuccessLoading get(): Boolean = !isLoading && !hasErrorLoading
}

class ClienteFormViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "ClienteFormViewModel"
    private val clienteId: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0

    var uiState: ClienteFormUiState by mutableStateOf(ClienteFormUiState())

    init {
        if (clienteId > 0) {
            loadCliente()
        }
    }

    fun loadCliente() {
        uiState = uiState.copy(
            isLoading = true,
            hasErrorLoading = false,
            clienteId = clienteId
        )
        viewModelScope.launch {
            delay(2000)
            uiState = try {
                val cliente = ApiService.clientes.findById(clienteId)
                uiState.copy(
                    isLoading = false,
                    formState = FormState(
                        nome = FormField(cliente.nome),
                        cpf = FormField(cliente.cpf),
                        telefone = FormField(cliente.telefone),
                        cep = FormField(cliente.endereco.cep),
                        logradouro = FormField(cliente.endereco.logradouro),
                        numero = FormField(cliente.endereco.numero.toString()),
                        complemento = FormField(cliente.endereco.complemento),
                        bairro = FormField(cliente.endereco.bairro),
                        cidade = FormField(cliente.endereco.cidade)
                    )
                )
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao carregar os dados do cliente com código $clienteId", ex)
                uiState.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun onNomeChanged(value: String) {
        if (uiState.formState.nome.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    nome = uiState.formState.nome.copy(
                        value = value,
                        errorMessageCode = validateNome(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateNome(nome: String): Int? = if (nome.isBlank()) {
        R.string.nome_obrigatorio
    } else {
        null
    }

    fun onCpfChanged(value: String) {
        if (uiState.formState.cpf.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    cpf = uiState.formState.cpf.copy(
                        value = value,
                        errorMessageCode = validateCpf(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateCpf(cpf: String): Int? = if (cpf.isBlank()) {
        R.string.cpf_obrigatorio
    } else if (cpf.length != 11) {
        R.string.cpf_invalido
    } else {
        null
    }

    fun onTelefoneChanged(value: String) {
        if (uiState.formState.telefone.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    telefone = uiState.formState.telefone.copy(
                        value = value,
                        errorMessageCode = validateTelefone(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateTelefone(telefone: String): Int? = if (telefone.isBlank()) {
        R.string.telefone_obrigatorio
    } else if (telefone.length < 10 || telefone.length > 11) {
        R.string.telefone_invalido
    } else {
        null
    }

    fun onCepChanged(value: String) {
        if (uiState.formState.cep.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    cep = uiState.formState.cep.copy(
                        value = value,
                        errorMessageCode = validateCep(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateCep(cep: String): Int? = if (cep.isBlank()) {
        R.string.cep_obrigatorio
    } else if (cep.length != 8) {
        R.string.cep_invalido
    } else {
        null
    }

    fun onNumeroChanged(value: String) {
        if (uiState.formState.numero.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    numero = uiState.formState.numero.copy(
                        value = value,
                        errorMessageCode = validateNumero(value)
                    )
                )
            )
        }
    }

    @StringRes
    private fun validateNumero(numero: String): Int? = if (numero.isBlank()) {
        R.string.numero_obrigatorio
    } else {
        null
    }

    fun onComplementoChanged(value: String) {
        if (uiState.formState.complemento.value != value) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    complemento = uiState.formState.complemento.copy(
                        value = value
                    )
                )
            )
        }
    }

    fun save() {
        if (!isValidForm()) {
            return
        }
        uiState = uiState.copy(
            isSaving = true,
            hasErrorSaving = false
        )
        viewModelScope.launch {
            delay(2000)
            val cliente = Cliente(
                id = clienteId,
                nome = uiState.formState.nome.value,
                cpf = uiState.formState.cpf.value,
                telefone = uiState.formState.telefone.value,
                endereco = Endereco(
                    cep = uiState.formState.cep.value,
                    logradouro = uiState.formState.logradouro.value,
                    numero = uiState.formState.numero.value.toIntOrNull() ?: 0,
                    complemento = uiState.formState.complemento.value,
                    bairro = uiState.formState.bairro.value,
                    cidade = uiState.formState.cidade.value
                )
            )
            uiState = try {
                val response = ApiService.clientes.save(cliente)
                if (response.isSuccessful) {
                    uiState.copy(
                        isSaving = false,
                        clienteSaved = true
                    )
                } else if (response.code() == 400) {
                    val error = Json.parseToJsonElement(response.errorBody()!!.string())
                    val jsonObject = error.jsonObject
                    val apiValidationError =
                        jsonObject.keys.joinToString("\n") {
                            jsonObject[it].toString().replace("\"", "")
                        }
                    uiState.copy(
                        isSaving = false,
                        apiValidationError = apiValidationError
                    )
                } else {
                    uiState.copy(
                        isSaving = false,
                        hasErrorSaving = true
                    )
                }
            } catch (ex: Exception) {
                Log.d(tag, "Erro ao salvar cliente com código $clienteId", ex)
                uiState.copy(
                    isSaving = false,
                    hasErrorSaving = true
                )
            }
        }
    }

    private fun isValidForm(): Boolean {
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                nome = uiState.formState.nome.copy(
                    errorMessageCode = validateNome(uiState.formState.nome.value)
                ),
                cpf = uiState.formState.cpf.copy(
                    errorMessageCode = validateCpf(uiState.formState.cpf.value)
                ),
                telefone = uiState.formState.telefone.copy(
                    errorMessageCode = validateTelefone(uiState.formState.telefone.value)
                ),
                cep = uiState.formState.cep.copy(
                    errorMessageCode = validateCep(uiState.formState.cep.value)
                ),
                numero = uiState.formState.numero.copy(
                    errorMessageCode = validateNumero(uiState.formState.numero.value)
                )
            )
        )
        return uiState.formState.isValid
    }

    fun dismissInformationDialog() {
        uiState = uiState.copy(
            apiValidationError = ""
        )
    }
}