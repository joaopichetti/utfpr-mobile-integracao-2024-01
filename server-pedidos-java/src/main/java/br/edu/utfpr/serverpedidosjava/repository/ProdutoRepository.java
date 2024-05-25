package br.edu.utfpr.serverpedidosjava.repository;

import br.edu.utfpr.serverpedidosjava.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    @Query("""
        SELECT p FROM Produto p
        WHERE CAST(p.id AS String) LIKE CONCAT('%', :query, '%')
            OR UPPER(p.nome) LIKE UPPER(CONCAT('%', :query, '%'))
            OR CAST(p.preco AS String) LIKE CONCAT('%', :query, '%')
            OR UPPER(p.categoria.nome) LIKE UPPER(CONCAT('%', :query, '%'))
    """)
    public List<Produto> findByQuery(@Param("query") String query);
}
