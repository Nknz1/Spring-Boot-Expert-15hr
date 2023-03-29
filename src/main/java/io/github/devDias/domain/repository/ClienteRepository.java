package io.github.devDias.domain.repository;

import io.github.devDias.domain.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    @Query("select c from ClienteEntity c left join fetch  c.pedidos where c.id = :id ")
    ClienteEntity findClienteFetchPedidos(@Param("id") Integer id );


}
