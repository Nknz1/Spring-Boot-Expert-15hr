package io.github.devDias;

import io.github.devDias.domain.entity.ClienteEntity;
import io.github.devDias.domain.entity.ProdutoEntity;
import io.github.devDias.domain.repository.ClienteRepository;
import io.github.devDias.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VendasApplication {

    @Bean
    public CommandLineRunner commandLineRunner(@Autowired ClienteRepository cliente,
                                               @Autowired ProdutoRepository produto) {
        return args -> {

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }
}
