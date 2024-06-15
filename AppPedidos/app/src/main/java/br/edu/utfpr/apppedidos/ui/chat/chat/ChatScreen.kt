package br.edu.utfpr.apppedidos.ui.chat.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.apppedidos.R
import br.edu.utfpr.apppedidos.data.chat.ChatMensagem
import br.edu.utfpr.apppedidos.data.usuario.Usuario
import br.edu.utfpr.apppedidos.extensions.toFormat
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultErrorLoading
import br.edu.utfpr.apppedidos.ui.utils.composables.DefaultLoading

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory)
) {
    val context = LocalContext.current
    LaunchedEffect(snackbarHostState, viewModel.uiState.hasErrorSending) {
        if (viewModel.uiState.hasErrorSending) {
            snackbarHostState.showSnackbar(context.getString(R.string.erro_enviar_mensagem))
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                title = viewModel.uiState.chat.getNome(viewModel.uiState.usuario),
                onBackPressed = onBackPressed
            )
        },
        bottomBar = {
            if (viewModel.uiState.isSuccess) {
                BottomBar(
                    onSendPressed = viewModel::sendMensagem,
                    mensagem = viewModel.uiState.mensagem,
                    onMensagemChanged = viewModel::onMensagemChanged,
                    isSending = viewModel.uiState.isSending
                )
            }
        }
    ) { innerPadding ->
        if (viewModel.uiState.isLoading) {
            DefaultLoading(
                modifier = Modifier.padding(innerPadding),
                text = "${stringResource(R.string.carregando_mensagens)}..."
            )
        } else if (viewModel.uiState.hasErrorLoading) {
            DefaultErrorLoading(
                modifier = Modifier.padding(innerPadding),
                text = stringResource(R.string.erro_carregar_mensagens),
                onTryAgainPressed = viewModel::load
            )
        } else {
            Mensagens(
                modifier = Modifier.padding(innerPadding),
                mensagens = viewModel.uiState.mensagens,
                usuarioAtual = viewModel.uiState.usuario
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.voltar)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    AppPedidosTheme {
        TopBar(
            title = "João",
            onBackPressed = {}
        )
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    onSendPressed: () -> Unit,
    mensagem: String,
    onMensagemChanged: (String) -> Unit,
    isSending: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(all = 16.dp)
                .background(Color.White),
            value = mensagem,
            onValueChange = onMensagemChanged,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            enabled = !isSending
        )
        if (isSending) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
                    .padding(all = 16.dp),
                strokeWidth = 2.dp
            )
        } else {
            IconButton(
                onClick = onSendPressed,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = stringResource(R.string.enviar)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    AppPedidosTheme {
        BottomBar(
            onSendPressed = {},
            mensagem = "",
            onMensagemChanged = {},
            isSending = false
        )
    }
}

@Composable
private fun Mensagens(
    modifier: Modifier = Modifier,
    mensagens: List<ChatMensagem>,
    usuarioAtual: Usuario
) {
    if (mensagens.isEmpty()) {
        EmptyList(modifier = modifier)
    } else {
        FilledList(
            modifier = modifier,
            mensagens = mensagens,
            usuarioAtual = usuarioAtual
        )
    }
}

@Composable
private fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.envie_sua_primeira_mensagem),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun EmptyListPreview() {
    AppPedidosTheme {
        EmptyList()
    }
}

@Composable
fun FilledList(
    modifier: Modifier = Modifier,
    mensagens: List<ChatMensagem>,
    usuarioAtual: Usuario
) {
    LazyColumn(
        modifier = modifier.padding(all = 16.dp),
        reverseLayout = true
    ) {
        items(mensagens) { mensagem ->
            val isFromUsuarioAtual = usuarioAtual.id == mensagem.usuario.id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = if (isFromUsuarioAtual) {
                    Arrangement.End
                } else {
                    Arrangement.Start
                }
            ) {
                Column(
                    modifier = Modifier
                        .sizeIn(maxWidth = 300.dp)
                        .background(
                            color = if (isFromUsuarioAtual) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            },
                            shape = RoundedCornerShape(16.dp)
                        ),
                    horizontalAlignment = if (isFromUsuarioAtual) {
                        Alignment.End
                    } else {
                        Alignment.Start
                    }
                ) {
                    Text(
                        text = mensagem.mensagem,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isFromUsuarioAtual) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                    Text(
                        text = mensagem.dataHora.toFormat("dd/MM/yyyy HH:mm"),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun FilledListPreview() {
    val usuarios = listOf(
        Usuario(
            id = 1,
            "Usuário 1"
        ),
        Usuario(
            id = 2,
            "Usuário 2"
        )
    )
    AppPedidosTheme {
        FilledList(
            mensagens = listOf(
                ChatMensagem(
                    mensagem = "Boa tarde! Tudo bem?",
                    dataHora = "2023-06-12 21:59:00",
                    usuario = usuarios[0]
                ),
                ChatMensagem(
                    mensagem = "Opa! Tudo bem e você?",
                    dataHora = "2023-06-12 22:00",
                    usuario = usuarios[1]
                ),
                ChatMensagem(
                    mensagem = "Estou bem, também!",
                    dataHora = "2023-06-12 22:02",
                    usuario = usuarios[0]
                ),
                ChatMensagem(
                    mensagem = "Posso te ligar rapidinho?",
                    dataHora = "2023-06-12 22:04",
                    usuario = usuarios[0]
                ),
                ChatMensagem(
                    mensagem = "Pode sim!",
                    dataHora = "2023-06-12 22:05",
                    usuario = usuarios[1]
                )
            ),
            usuarioAtual = usuarios[0]
        )
    }
}












