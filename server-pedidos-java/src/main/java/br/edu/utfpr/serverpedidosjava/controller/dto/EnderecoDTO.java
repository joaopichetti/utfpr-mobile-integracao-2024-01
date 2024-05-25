package br.edu.utfpr.serverpedidosjava.controller.dto;

import br.edu.utfpr.serverpedidosjava.entity.Cliente;
import br.edu.utfpr.serverpedidosjava.entity.Endereco;
import br.edu.utfpr.serverpedidosjava.util.validator.CEP;
import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
        @NotBlank(message = "{cep.notblank}") @CEP String cep,
        @NotBlank(message = "{logradouro.notblank}") String logradouro,
        Integer numero,
        String complemento,
        String bairro,
        @NotBlank(message = "{cidade.notblank}") String cidade) {

    public Endereco toEntity(Cliente cliente) {
        Endereco endereco = new Endereco();
        endereco.setId(cliente.getId());
        endereco.setCep(this.cep);
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setCliente(cliente);
        return endereco;
    }

    public static EnderecoDTO fromEntity(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade());
    }
}
