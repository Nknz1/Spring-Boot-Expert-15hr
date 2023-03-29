package io.github.devDias.rest.controller;

import io.github.devDias.domain.entity.ClienteEntity;
import io.github.devDias.domain.entity.ProdutoEntity;
import io.github.devDias.domain.repository.ClienteRepository;
import io.github.devDias.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    private final static String MSG_NAO_ENCONTRADO = "Produto não encontrado";

    @GetMapping(value = "{id}")
    public ProdutoEntity obterProdutoPorId(@PathVariable Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO));


    };
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoEntity salvarProduto (@RequestBody @Valid ProdutoEntity produto) {
            return produtoRepository.save(produto);

    };

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProduto(@PathVariable Integer id) {
        produtoRepository.findById(id)
                .map(produto -> { produtoRepository.delete(produto);
                    return produto;})
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO);
                });

    };

    @PutMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarProduto( @PathVariable Integer id,
                                            @RequestBody @Valid ProdutoEntity produto) {

        produtoRepository
                .findById(id)
                .map(produtoExiste -> {
                    produto.setId(produtoExiste.getId());
                    produtoRepository.save(produto);
                    return produtoExiste;
                }).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO);
        });

    }
    @GetMapping("/filtro")
    public List pesquisarProdutoPorFiltro(ProdutoEntity filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro,matcher);
        List produtoEncontrado = produtoRepository.findAll(example);
        if(produtoEncontrado.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO);
        }
        return produtoEncontrado;
    };

    @GetMapping()
    public List pesquisarTodosProdutos() {
        return produtoRepository.findAll();
    };


}
