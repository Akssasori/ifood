package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.excption.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurante> listar() {
        return cadastroRestaurante.listar();
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> buscar(@PathVariable("restauranteId") Long restauranteId) {

        Restaurante restaurante = restauranteRepository.buscar(restauranteId);

        if(restaurante != null) {
            Restaurante buscar = cadastroRestaurante.buscar(restauranteId);
            return ResponseEntity.ok(buscar);
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {

        try {
            restaurante = cadastroRestaurante.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable("restauranteId") Long restauranteId,
                                                 @RequestBody Restaurante restaurante) {

        Restaurante restauranteDb = restauranteRepository.buscar(restauranteId);

        try {

            if (restauranteDb != null) {
                restauranteDb.setNome(restaurante.getNome());
                restauranteDb.setTaxaFrete(restaurante.getTaxaFrete());
                restauranteDb.getCozinha().setId(restaurante.getCozinha().getId());
                cadastroRestaurante.salvar(restauranteDb);
                return ResponseEntity.ok(restauranteDb);
            }

        }catch (DataIntegrityViolationException e) {
            throw new EntidadeNaoEncontradaException(String.format("Cozinha de código %d não pode ser removida, pois está em uso", restaurante.getCozinha().getId()));
        }catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }
}
