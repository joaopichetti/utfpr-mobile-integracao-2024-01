package br.edu.utfpr.serverpedidosjava.controller;

import br.edu.utfpr.serverpedidosjava.controller.dto.PedidoDTO;
import br.edu.utfpr.serverpedidosjava.entity.Pedido;
import br.edu.utfpr.serverpedidosjava.repository.PedidoRepository;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public List<PedidoDTO> list() {
        return this.pedidoRepository.findAll().stream().map(PedidoDTO::fromEntity).toList();
    }

    @GetMapping("/page")
    public Page<PedidoDTO> paginar(Pageable pageable) {
        return this.pedidoRepository.findAll(pageable).map(PedidoDTO::fromEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable(name = "id") Integer id) {
        Pedido pedido = this.pedidoRepository.findById(id).orElse(null);
        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(PedidoDTO.fromEntity(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {
        this.pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public PedidoDTO save(@Valid @RequestBody PedidoDTO pedido) {
        Pedido pedidoAtualizado = this.pedidoRepository.save(pedido.toEntity());
        return PedidoDTO.fromEntity(pedidoAtualizado);
    }
}
