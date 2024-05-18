package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Cliente

data class ClienteDTO(
    var id: Int = 0,
    var nome: String = "",
    var cpf: String = "",
    var telefone: String = "",
    var endereco: EnderecoDTO? = null
) {
    fun toEntity(): Cliente = Cliente(
        id = this.id,
        nome = this.nome,
        cpf = this.cpf,
        telefone = this.telefone
    ).also {
        it.endereco = endereco?.toEntity(it)
    }

    companion object {
        fun fromEntity(cliente: Cliente): ClienteDTO = ClienteDTO(
            id = cliente.id,
            nome = cliente.nome,
            cpf = cliente.cpf,
            telefone = cliente.telefone,
            endereco = cliente.endereco?.let { EnderecoDTO.fromEntity(it) }
        )
    }
}
