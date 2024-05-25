package br.edu.utfpr.serverpedidosjava.repository;

import br.edu.utfpr.serverpedidosjava.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {}
