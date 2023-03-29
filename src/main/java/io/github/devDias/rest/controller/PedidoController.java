package io.github.devDias.rest.controller;


import io.github.devDias.domain.entity.ItemPedidoEntity;
import io.github.devDias.domain.entity.PedidoEntity;
import io.github.devDias.domain.entity.enums.StatusPedido;
import io.github.devDias.domain.service.PedidoService;
import io.github.devDias.rest.dto.AtualizacaoStatusPedidoDTO;
import io.github.devDias.rest.dto.InformacaoItemPedidoDTO;
import io.github.devDias.rest.dto.InformacoesPedidoDTO;
import io.github.devDias.rest.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Integer salvarPedido(@RequestBody @Valid PedidoDTO pedidoDTO) {
            PedidoEntity pedido = pedidoService.salvarPedido(pedidoDTO);
            return pedido.getId();
    };

    @GetMapping("{id}")
    public InformacoesPedidoDTO obterPorId(@PathVariable() Integer id) {
        return pedidoService
                .obterPedidoCompleto(id)
                .map( p -> conveter(p))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Pedido Não Encontrado."));

    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePedido(@PathVariable Integer id,
                             @RequestBody @Valid AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        pedidoService.atualizaStatus(id, StatusPedido.valueOf(novoStatus));


    }

    private InformacoesPedidoDTO conveter(PedidoEntity pedido) {
        return InformacoesPedidoDTO
                .builder()
                .id(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();

    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedidoEntity> itens) {
            if(CollectionUtils.isEmpty(itens)) {
                return Collections.emptyList();
            }

            return itens
                    .stream()
                    .map( item -> InformacaoItemPedidoDTO
                    .builder()
                            .descricaoProduto(item.getProduto().getDescricao())
                            .precoUnitario(item.getProduto().getPreco())
                            .quantidade(item.getQuantidade())
                            .build()
                    ).collect(Collectors.toList());
    };


}
