package br.edu.utfpr.serverpedidos.repository

import br.edu.utfpr.serverpedidos.entity.Pedido
import org.springframework.data.jpa.repository.JpaRepository

interface PedidoRepository : JpaRepository<Pedido, Int>
