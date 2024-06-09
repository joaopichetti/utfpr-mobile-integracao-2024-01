package br.edu.utfpr.apppedidos.ui.cliente.form

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultErrorLoading
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultLoading

@Composable
fun ClienteFormScreen(
    modifier: Modifier = Modifier,
    viewModel: ClienteFormViewModel = viewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackPressed: () -> Unit,
    onClienteSaved: () -> Unit
) {
    LaunchedEffect(viewModel.uiState.clienteSaved) {
        if (viewModel.uiState.clienteSaved) {
            onClienteSaved()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorSaving) {
        if (viewModel.uiState.hasErrorSaving) {
            snackbarHostState.showSnackbar(
                context.getString(R.string.erro_salvar_cliente)
            )
        }
    }

    if (viewModel.uiState.apiValidationError.isNotBlank()) {
        InformationDialog(
            text = viewModel.uiState.apiValidationError,
            onDismiss = viewModel::dismissInformationDialog
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            FormTopAppBar(
                isNewCliente = viewModel.uiState.isNewCliente,
                onBackPressed = onBackPressed,
                onSavePressed = viewModel::save,
                showActions = viewModel.uiState.isSuccessLoading,
                isSaving = viewModel.uiState.isSaving
            )
        }
    ) { innerPadding ->
        if (viewModel.uiState.isLoading) {
            DefaultLoading(text = stringResource(R.string.carregando))
        } else if (viewModel.uiState.hasErrorLoading) {
            DefaultErrorLoading(
                text = stringResource(R.string.erro_ao_carregar),
                onTryAgainPressed = viewModel::loadCliente
            )
        } else {
            FormContent(
                modifier = Modifier.padding(innerPadding),
                clienteId = viewModel.uiState.clienteId,
                formState = viewModel.uiState.formState,
                onNomeChanged = viewModel::onNomeChanged,
                onCpfChanged = viewModel::onCpfChanged,
                onTelefoneChanged = viewModel::onTelefoneChanged,
                onCepChanged = viewModel::onCepChanged,
                onNumeroChanged = viewModel::onNumeroChanged,
                onComplementoChanged = viewModel::onComplementoChanged
            )
        }
    }
}

@Composable
private fun InformationDialog(
    modifier: Modifier = Modifier,
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(stringResource(R.string.mensagem_retornada_do_servidor))
        },
        text = {
            Text(text)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTopAppBar(
    modifier: Modifier = Modifier,
    isNewCliente: Boolean,
    showActions: Boolean,
    isSaving: Boolean,
    onBackPressed: () -> Unit,
    onSavePressed: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(if (isNewCliente) {
                stringResource(R.string.novo_cliente)
            } else {
                stringResource(R.string.editar_cliente)
            })
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.voltar)
                )
            }
        },
        actions = {
            if (showActions) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(all = 16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onSavePressed) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.salvar)
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun FormTopAppBarPeview() {
    AppPedidosTheme {
        FormTopAppBar(
            isNewCliente = true,
            showActions = true,
            isSaving = false,
            onBackPressed = {},
            onSavePressed = {},
        )
    }
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    clienteId: Int,
    formState: FormState,
    onNomeChanged: (String) -> Unit,
    onCpfChanged: (String) -> Unit,
    onTelefoneChanged: (String) -> Unit,
    onCepChanged: (String) -> Unit,
    onNumeroChanged: (String) -> Unit,
    onComplementoChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ClienteId(id = clienteId)
        FormTextField(
            label = stringResource(R.string.nome),
            value = formState.nome.value,
            onValueChanged = onNomeChanged,
            errorMessageCode = formState.nome.errorMessageCode,
            keyboardCapitalization = KeyboardCapitalization.Words
        )
        FormTextField(
            label = stringResource(R.string.cpf),
            value = formState.cpf.value,
            onValueChanged = onCpfChanged,
            errorMessageCode = formState.cpf.errorMessageCode,
            keyboardType = KeyboardType.Number
        )
        FormTextField(
            label = stringResource(R.string.telefone),
            value = formState.telefone.value,
            onValueChanged = onTelefoneChanged,
            errorMessageCode = formState.telefone.errorMessageCode,
            keyboardType = KeyboardType.Phone
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        FormTitle(text = stringResource(R.string.endereco))
        FormTextField(
            label = stringResource(R.string.cep),
            value = formState.cep.value,
            onValueChanged = onCepChanged,
            errorMessageCode = formState.cep.errorMessageCode,
            keyboardType = KeyboardType.Number
        )
        FormTextField(
            label = stringResource(R.string.logradouro),
            value = formState.logradouro.value,
            onValueChanged = {},
            enabled = false
        )
        FormTextField(
            label = stringResource(R.string.numero),
            value = formState.numero.value,
            onValueChanged = onNumeroChanged,
            errorMessageCode = formState.numero.errorMessageCode,
            keyboardType = KeyboardType.Number
        )
        FormTextField(
            label = stringResource(R.string.complemento),
            value = formState.complemento.value,
            onValueChanged = onComplementoChanged,
            errorMessageCode = formState.complemento.errorMessageCode,
            keyboardImeAction = ImeAction.Done
        )
        FormTextField(
            label = stringResource(R.string.bairro),
            value = formState.bairro.value,
            onValueChanged = {},
            enabled = false
        )
        FormTextField(
            label = stringResource(R.string.cidade),
            value = formState.cidade.value,
            onValueChanged = {},
            enabled = false
        )
    }
}

@Composable
private fun ClienteId(
    modifier: Modifier = Modifier,
    id: Int
) {
    if (id > 0) {
        FormTitle(text = "${stringResource(R.string.codigo)} - $id")
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormTitle(text = "${stringResource(R.string.codigo)} - ")
            Text(
                text = "a definir",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontStyle = FontStyle.Italic
                )
            )
        }
    }
}

@Composable
private fun FormTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(start = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun FormTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    enabled: Boolean = true,
    @StringRes
    errorMessageCode: Int? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardImeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onValueChange = onValueChanged,
        label = { Text(label) },
        maxLines = 1,
        enabled = enabled,
        isError = errorMessageCode != null,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = keyboardCapitalization,
            imeAction = keyboardImeAction,
            keyboardType = keyboardType
        ),
        visualTransformation = visualTransformation
    )
    errorMessageCode?.let {
        Text(
            text = stringResource(errorMessageCode),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormContentPreview() {
    AppPedidosTheme {
        FormContent(
            clienteId = 1,
            formState = FormState(),
            onNomeChanged = { },
            onCpfChanged = { },
            onTelefoneChanged = { },
            onCepChanged = { },
            onNumeroChanged = { },
            onComplementoChanged = { }
        )
    }
}
















