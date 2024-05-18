package br.edu.utfpr.serverpedidos.controller.dto

import br.edu.utfpr.serverpedidos.entity.Cliente
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF

data class ClienteDTO(
    var id: Int = 0,
    @field:NotBlank(message = "{nome.notblank}")
    var nome: String = "",
    @field:NotBlank(message = "{cpf.notblank}")
    @field:CPF(message = "{cpf.invalido}")
    var cpf: String = "",
    @field:NotBlank(message = "{telefone.notblank}")
    var telefone: String = "",
    @field:NotNull(message = "{endereco.notnull}")
    @field:Valid
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
