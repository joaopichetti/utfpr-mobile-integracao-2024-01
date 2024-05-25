package br.edu.utfpr.serverpedidosjava.repository;

import br.edu.utfpr.serverpedidosjava.entity.Categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    Page<Categoria> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
