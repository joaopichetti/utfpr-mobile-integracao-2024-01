package br.edu.utfpr.serverpedidosjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.serverpedidosjava.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
