package io.github.devDias.domain.repository;

import io.github.devDias.domain.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {

}
