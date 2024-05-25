package br.edu.utfpr.serverpedidosjava.controller.dto;

import br.edu.utfpr.serverpedidosjava.entity.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoDTO(
        Integer id,
        @NotBlank(message = "{nome.notblank}")
        @Size(max = 100, message = "{nome.size}")
        String nome,
        @Positive(message = "{preco.positive}")
        BigDecimal preco,
        @NotNull(message = "{categoria.notnull}")
        CategoriaDTO categoria
) {
    public Produto toEntity() {
        Produto produto = new Produto();
        produto.setId(this.id);
        produto.setNome(this.nome);
        produto.setPreco(this.preco);
        produto.setCategoria(this.categoria.toEntity());
        return produto;
    }

    public static ProdutoDTO fromEntity(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                CategoriaDTO.fromEntity(produto.getCategoria())
        );
    }
}
