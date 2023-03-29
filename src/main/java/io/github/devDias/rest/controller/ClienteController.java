package io.github.devDias.rest.controller;

import io.github.devDias.domain.entity.ClienteEntity;
import io.github.devDias.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    private final String MSG_NAO_ENCONTRADO = "Cliente não encontrado";

    @GetMapping(value = "{id}")
    public ClienteEntity obterClientePorId(@PathVariable Integer id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO));

    };

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteEntity salvarCliente (@RequestBody @Valid ClienteEntity cliente) {
            return clienteRepository.save(cliente);

    };

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCliente(@PathVariable Integer id) {
        clienteRepository.findById(id)
                .map(cliente -> { clienteRepository.delete(cliente);
                    return cliente;})
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO);
                });

    };

    @PutMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarCliente( @PathVariable Integer id,
                                            @RequestBody @Valid ClienteEntity cliente) {

        clienteRepository
                .findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clienteRepository.save(cliente);
                    return clienteExistente;
                }).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO);
        });

    }
    @GetMapping("/filtro")
    public List pesquisarClientePorFiltro(ClienteEntity filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro,matcher);
        List clienteEncontrado = clienteRepository.findAll(example);
        if(clienteEncontrado.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, MSG_NAO_ENCONTRADO);
        }

        return clienteEncontrado;
    };

    @GetMapping()
    public List pesquisarTodosClientes() {
        return clienteRepository.findAll();
    };


}
