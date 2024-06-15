package br.edu.utfpr.apppedidos.data.usuario.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import br.edu.utfpr.apppedidos.data.proto.Usuario
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UsuarioSerializer : Serializer<Usuario> {
    override val defaultValue: Usuario
        get() = Usuario.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Usuario {
        try {
            return Usuario.parseFrom(input)
        } catch (ex: InvalidProtocolBufferException) {
            throw CorruptionException("Não foi possível ler o arquivo proto", ex)
        }
    }

    override suspend fun writeTo(t: Usuario, output: OutputStream) = t.writeTo(output)
}

val Context.usuarioDataStore: DataStore<Usuario> by dataStore(
    fileName = "usuario.pb",
    serializer = UsuarioSerializer
)