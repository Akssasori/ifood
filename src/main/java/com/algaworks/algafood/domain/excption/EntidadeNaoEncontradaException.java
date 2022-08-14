package com.algaworks.algafood.domain.excption;

public class EntidadeNaoEncontradaException extends RuntimeException{

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
