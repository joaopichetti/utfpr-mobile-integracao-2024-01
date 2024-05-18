package br.edu.utfpr.serverpedidos.repository

import br.edu.utfpr.serverpedidos.entity.Cliente
import org.springframework.data.jpa.repository.JpaRepository

interface ClienteRepository : JpaRepository<Cliente, Int>
