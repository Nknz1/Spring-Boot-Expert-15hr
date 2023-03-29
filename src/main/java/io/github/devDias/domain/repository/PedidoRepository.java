package io.github.devDias.domain.repository;

import io.github.devDias.domain.entity.ClienteEntity;
import io.github.devDias.domain.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {

    @Query("select p from PedidoEntity p left join fetch p.itens where p.id = :id")
    Optional<PedidoEntity> findByIdFetchItens(@Param("id") Integer id);

}
