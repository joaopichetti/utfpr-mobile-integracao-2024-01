package br.edu.utfpr.serverpedidosjava.controller.dto;

import br.edu.utfpr.serverpedidosjava.entity.Categoria;
import jakarta.validation.constraints.NotBlank;

public record CategoriaDTO(
        Integer id,
        @NotBlank(message = "{nome.notblank}") String nome) {

    public Categoria toEntity() {
        Categoria categoria = new Categoria();
        categoria.setId(this.id);
        categoria.setNome(this.nome);
        return categoria;
    }

    public static CategoriaDTO fromEntity(Categoria categoria) {
        return new CategoriaDTO(categoria.getId(), categoria.getNome());
    }
}
