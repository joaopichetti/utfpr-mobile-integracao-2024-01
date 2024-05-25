package br.edu.utfpr.serverpedidosjava.controller;

import br.edu.utfpr.serverpedidosjava.controller.dto.ClienteDTO;
import br.edu.utfpr.serverpedidosjava.entity.Cliente;
import br.edu.utfpr.serverpedidosjava.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<ClienteDTO> list() {
        return this.clienteRepository
                .findAll()
                .stream()
                .map(ClienteDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable(name = "id") Integer id) {
        Cliente cliente = this.clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ClienteDTO.fromEntity(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {
        this.clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ClienteDTO save(@Valid @RequestBody ClienteDTO cliente) {
        Cliente clienteAtualizado = this.clienteRepository.save(cliente.toEntity());
        return ClienteDTO.fromEntity(clienteAtualizado);
    }
}
