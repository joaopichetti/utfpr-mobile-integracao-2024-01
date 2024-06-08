package br.edu.utfpr.apppedidos.ui.cliente.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.data.cliente.Cliente
import br.edu.utfpr.apppedidos.data.cliente.Endereco
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultErrorLoading
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultLoaing

@Composable
fun ClienteDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: ClienteDetailsViewModel = viewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackPressed: () -> Unit,
    onClienteDeleted: () -> Unit
) {
    LaunchedEffect(viewModel.uiState.clienteDeleted) {
        if (viewModel.uiState.clienteDeleted) {
            onClienteDeleted()
        }
    }

    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorDeleting) {
        if (viewModel.uiState.hasErrorDeleting) {
            snackbarHostState.showSnackbar(context.getString(R.string.erro_remover_cliente))
        }
    }

    if (viewModel.uiState.showConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.atencao),
            text = stringResource(R.string.confirmar_remover_cliente),
            onDismiss = viewModel::dismissConfirmationDialog,
            onConfirm = viewModel::delete
        )
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            DetailsTopBar(
                showActions = viewModel.uiState.isSuccessLoading,
                isProcessing = viewModel.uiState.isDeleting,
                onBackPressed = onBackPressed,
                onEditPressed = {},
                onDeletePressed = viewModel::showConfirmationDialog
            )
        }
    ) { innerPadding ->
        if (viewModel.uiState.isLoading) {
            DefaultLoaing(
                modifier = Modifier.padding(innerPadding),
                text = stringResource(id = R.string.carregando)
            )
        } else if (viewModel.uiState.hasErrorLoading) {
            DefaultErrorLoading(
                text = stringResource(id = R.string.erro_ao_carregar),
                onTryAgainPressed = viewModel::loadCliente
            )
        } else {
            ClienteDetails(
                modifier = modifier.padding(innerPadding),
                cliente = viewModel.uiState.cliente
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    modifier: Modifier = Modifier,
    showActions: Boolean,
    isProcessing: Boolean,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onDeletePressed: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(text = stringResource(R.string.detalhes_do_cliente))
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
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(all = 16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onEditPressed) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.editar)
                        )
                    }
                    IconButton(onClick = onDeletePressed) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.remover)
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    confirmButtonText: String? = null
) {
    AlertDialog(
        modifier = modifier,
        title = title?.let {
            { Text(it) }
        },
        text = { Text(text) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmButtonText ?: stringResource(R.string.confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissButtonText ?: stringResource(R.string.cancelar))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailsTopBarPreview() {
    AppPedidosTheme {
        DetailsTopBar(
            showActions = true,
            isProcessing = false,
            onBackPressed = {},
            onEditPressed = {},
            onDeletePressed = {}
        )
    }
}

@Composable
fun ClienteDetails(
    modifier: Modifier = Modifier,
    cliente: Cliente
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ClienteTitle(text = "${stringResource(R.string.codigo)} - ${cliente.id}")
        ClienteAttribute(
            attributeName = "Nome",
            attributeValue = cliente.nome
        )
        ClienteAttribute(
            attributeName = "CPF",
            attributeValue = cliente.cpf
        )
        ClienteAttribute(
            attributeName = "Telefone",
            attributeValue = cliente.telefone
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
        ClienteTitle(text = "Endereço")
        ClienteAttribute(
            attributeName = "CEP",
            attributeValue = cliente.endereco.cep
        )
        ClienteAttribute(
            attributeName = "Logradouro",
            attributeValue = cliente.endereco.cep
        )
        ClienteAttribute(
            attributeName = "Número",
            attributeValue = cliente.endereco.numero.toString()
        )
        ClienteAttribute(
            attributeName = "Complemento",
            attributeValue = cliente.endereco.complemento
        )
        ClienteAttribute(
            attributeName = "Bairro",
            attributeValue = cliente.endereco.bairro
        )
        ClienteAttribute(
            attributeName = "Cidade",
            attributeValue = cliente.endereco.cidade
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun ClienteDetailsPreview() {
    AppPedidosTheme {
        ClienteDetails(cliente = Cliente(
            id = 1,
            nome = "João",
            endereco = Endereco(
                logradouro = "Rua Tocantins",
                numero = 3435,
                cidade = "Pato Branco - PR"
            )
        ))
    }
}

@Composable
private fun ClienteTitle(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(all = 16.dp)
    )
}

@Composable
private fun ClienteAttribute(
    modifier: Modifier = Modifier,
    attributeName: String,
    attributeValue: String
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = attributeName,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        val textStyle: TextStyle
        val text: String
        if (attributeValue.isNotEmpty()) {
            text = attributeValue
            textStyle = MaterialTheme.typography.labelLarge
        } else {
            text = "Não informado"
            textStyle = MaterialTheme.typography.labelSmall.copy(
                fontStyle = FontStyle.Italic
            )
        }
        Text(
            text = text,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary
        )
    }
}








