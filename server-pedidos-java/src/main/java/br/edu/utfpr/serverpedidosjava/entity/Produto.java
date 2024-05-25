package br.edu.utfpr.serverpedidosjava.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "meu_produto")
public class Produto {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "nome_produto", nullable = false, length = 100)
    private String nome;
    @Column(name = "valor_venda", nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
