package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Cliente
import br.edu.utfpr.serverpedidos.entity.Endereco
import jakarta.validation.constraints.NotBlank

data class EnderecoDTO(
    @field:NotBlank(message = "{cep.notblank}")
    var cep: String = "",
    @field:NotBlank(message = "{logradouro.notblank}")
    var logradouro: String = "",
    var numero: Int = 0,
    var complemento: String = "",
    var bairro: String = "",
    @field:NotBlank(message = "{cidade.notblank}")
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
