package br.edu.utfpr.serverpedidosjava.controller;

import br.edu.utfpr.serverpedidosjava.controller.dto.CategoriaDTO;
import br.edu.utfpr.serverpedidosjava.entity.Categoria;
import br.edu.utfpr.serverpedidosjava.repository.CategoriaRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public List<CategoriaDTO> list(@RequestParam(name = "nome") @Nullable String nome) {
        List<Categoria> categorias;
        if (Strings.isNotBlank(nome)) {
            categorias = categoriaRepository.findByNomeContainingIgnoreCase(nome);
        } else {
            categorias = categoriaRepository.findAll();
        }
        return categorias.stream().map(CategoriaDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable(name = "id") Integer id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(CategoriaDTO.fromEntity(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {
        categoriaRepository.findById(id).ifPresent(categoriaRepository::delete);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public CategoriaDTO save(@Valid @RequestBody CategoriaDTO categoria) {
        Categoria categoriaAtualizada = categoriaRepository.save(categoria.toEntity());
        return CategoriaDTO.fromEntity(categoriaAtualizada);
    }
}
