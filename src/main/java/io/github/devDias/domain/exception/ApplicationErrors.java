package io.github.devDias.domain.exception;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApplicationErrors {

    @Getter
    private List<String> mensagem;

    public ApplicationErrors(List<String> mensagem) {
        this.mensagem = mensagem;
    }

    public ApplicationErrors(String mensagemErro) {
        this.mensagem = Arrays.asList(mensagemErro);
    }
}
