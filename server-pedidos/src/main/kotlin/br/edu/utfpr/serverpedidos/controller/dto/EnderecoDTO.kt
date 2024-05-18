package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Cliente
import br.edu.utfpr.serverpedidos.entity.Endereco

data class EnderecoDTO(
    var cep: String = "",
    var logradouro: String = "",
    var numero: Int = 0,
    var complemento: String = "",
    var bairro: String = "",
    var cidade: String = ""
) {
    fun toEntity(cliente: Cliente): Endereco = Endereco(
        id = cliente.id,
        cep = this.cep,
        logradouro = this.logradouro,
        numero = this.numero,
        complemento = this.complemento,
        bairro = this.bairro,
        cidade = this.cidade,
        cliente = cliente
    )

    companion object {
        fun fromEntity(endereco: Endereco): EnderecoDTO = EnderecoDTO(
            cep = endereco.cep,
            logradouro = endereco.logradouro,
            numero = endereco.numero,
            complemento = endereco.complemento,
            bairro = endereco.bairro,
            cidade = endereco.cidade
        )
    }
}
