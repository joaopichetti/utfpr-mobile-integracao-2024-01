package br.edu.utfpr.serverpedidos.repository

import br.edu.utfpr.serverpedidos.entity.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProdutoRepository : JpaRepository<Produto, Int> {
    @Query("""
        SELECT p FROM Produto p
        WHERE CAST(p.id AS String) LIKE CONCAT('%', :query, '%')
            OR UPPER(p.nome) LIKE UPPER(CONCAT('%', :query, '%'))
            OR CAST(p.preco AS String) LIKE CONCAT('%', :query, '%')
            OR UPPER(p.categoria.nome) LIKE UPPER(CONCAT('%', :query, '%'))
    """)
    fun findByQuery(@Param("query") query: String): List<Produto>
}
