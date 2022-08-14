package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.excption.EntidadeEmUsoException;
import com.algaworks.algafood.domain.excption.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroCozinhaService {

    @Autowired
    private CozinhaRepository cozinhas;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhas.salvar(cozinha);
    }

//    public List<Cozinha> listar() {
//        return cozinhas.listar();
//    }
//
//    public Cozinha buscar(Long id) {
//        return cozinhas.buscar(id);
//    }

    public void excluir(Long cozinhaId) {

        try {
            cozinhas.remover(cozinhaId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida, pois está em uso", cozinhaId));
        } catch (EmptyResultDataAccessException e) {
            throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));
        }

    }

//    public Cozinha atualizar(Long id, Cozinha cozinha) {
//
//    }
}
