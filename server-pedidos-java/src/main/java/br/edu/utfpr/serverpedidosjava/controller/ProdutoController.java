package br.edu.utfpr.serverpedidosjava.controller;

import br.edu.utfpr.serverpedidosjava.controller.dto.ProdutoDTO;
import br.edu.utfpr.serverpedidosjava.entity.Produto;
import br.edu.utfpr.serverpedidosjava.repository.ProdutoRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public List<ProdutoDTO> list(@RequestParam(name = "query") @Nullable String query) {
        List<Produto> produtos;
        if (Strings.isNotBlank(query)) {
            produtos = this.produtoRepository.findByQuery(query);
        } else {
            produtos = this.produtoRepository.findAll();
        }
        return produtos.stream().map(ProdutoDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> findById(@PathVariable(name = "id") Integer id) {
        Produto produto = this.produtoRepository.findById(id).orElse(null);
        if (produto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProdutoDTO.fromEntity(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id) {
        this.produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ProdutoDTO save(@Valid @RequestBody ProdutoDTO produto) {
        Produto produtoAtualizado = this.produtoRepository.save(produto.toEntity());
        return ProdutoDTO.fromEntity(produtoAtualizado);
    }
}
