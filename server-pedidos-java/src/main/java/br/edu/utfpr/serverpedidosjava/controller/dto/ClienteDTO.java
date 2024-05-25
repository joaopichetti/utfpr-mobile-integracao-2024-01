package br.edu.utfpr.serverpedidosjava.controller.dto;

import br.edu.utfpr.serverpedidosjava.entity.Cliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteDTO(
        Integer id,
        @NotBlank(message = "{nome.notblank}")
        String nome,
        @NotBlank(message = "{cpf.notblank}")
        @CPF(message = "{cpf.invalido}")
        String cpf,
        @NotBlank(message = "{telefone.notblank}")
        String telefone,
        @NotNull(message = "{endereco.notnull}")
        @Valid
        EnderecoDTO endereco
) {
    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(this.id);
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setTelefone(this.telefone);
        cliente.setEndereco(this.endereco.toEntity(cliente));
        return cliente;
    }

    public static ClienteDTO fromEntity(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                EnderecoDTO.fromEntity(cliente.getEndereco())
        );
    }
}
