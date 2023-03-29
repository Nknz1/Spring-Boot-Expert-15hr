package io.github.devDias.domain.service;

import io.github.devDias.domain.entity.PedidoEntity;
import io.github.devDias.domain.entity.enums.StatusPedido;
import io.github.devDias.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    PedidoEntity salvarPedido (PedidoDTO pedidoDTO);

    Optional<PedidoEntity> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);


}
