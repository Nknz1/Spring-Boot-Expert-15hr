package io.github.devDias.domain.service.impl;

import io.github.devDias.domain.entity.ClienteEntity;
import io.github.devDias.domain.entity.ItemPedidoEntity;
import io.github.devDias.domain.entity.PedidoEntity;
import io.github.devDias.domain.entity.ProdutoEntity;
import io.github.devDias.domain.entity.enums.StatusPedido;
import io.github.devDias.domain.exception.NegocioException;
import io.github.devDias.domain.repository.ClienteRepository;
import io.github.devDias.domain.repository.ItemPedidoRepository;
import io.github.devDias.domain.repository.PedidoRepository;
import io.github.devDias.domain.repository.ProdutoRepository;
import io.github.devDias.domain.service.PedidoService;
import io.github.devDias.domain.exception.PedidoNaoEncontradoException;
import io.github.devDias.rest.dto.ItemPedidoDTO;
import io.github.devDias.rest.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public PedidoEntity salvarPedido(PedidoDTO requestDTO) {

        Integer idCliente = requestDTO.getCliente();
        ClienteEntity cliente = clienteRepository
                .findById(idCliente)
                        .orElseThrow( () ->  new NegocioException("ID de cliente Inválido"));

        PedidoEntity pedido = new PedidoEntity();
        pedido.setTotal(requestDTO.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedidoEntity> itensPedido = converterItems(pedido,requestDTO.getItems());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedido);

        pedido.setItens(itensPedido);
        return pedido;
    }

    @Override
    public Optional<PedidoEntity> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id).map( pedido -> {
            pedido.setStatus(statusPedido);
            return pedidoRepository.save(pedido);
        }).orElseThrow(PedidoNaoEncontradoException::new);
    }

    private List<ItemPedidoEntity> converterItems(PedidoEntity pedidoEntity, List<ItemPedidoDTO> items) {
        if(items.isEmpty()) {
            throw new NegocioException("Não é possivel realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
            Integer idProduto = dto.getProduto();

            ProdutoEntity produto = produtoRepository
                    .findById(idProduto)
                    .orElseThrow( () ->  new NegocioException("ID de produto Inválido: " + idProduto));

            ItemPedidoEntity itemPedidoEntity = new ItemPedidoEntity();
            itemPedidoEntity.setQuantidade(dto.getQuantidade());
            itemPedidoEntity.setPedido(pedidoEntity);
            itemPedidoEntity.setProduto(produto);

            return itemPedidoEntity;
                }).collect(Collectors.toList());
    }
}
