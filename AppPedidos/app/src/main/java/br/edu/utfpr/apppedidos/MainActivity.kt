package br.edu.utfpr.apppedidos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.edu.utfpr.apppedidos.ui.AppPedidos
import br.edu.utfpr.apppedidos.ui.theme.AppPedidosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPedidosTheme {
                AppPedidos()
            }
        }
    }
}