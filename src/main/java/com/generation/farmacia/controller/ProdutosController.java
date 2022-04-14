package com.generation.farmacia.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.farmacia.model.Produtos;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutosRepository;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutosController {
	
	@Autowired
	private ProdutosRepository produtosRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ResponseEntity<List<Produtos>> getAll() {
		return ResponseEntity.ok(produtosRepository.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Produtos> getId(@PathVariable Long id) {
		return produtosRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/nomeproduto/{nomeproduto}")
	public ResponseEntity<List<Produtos>> getByNomeproduto(@PathVariable String nomeproduto){
		return ResponseEntity.ok(produtosRepository.findAllByNomeprodutoContainingIgnoreCase(nomeproduto));
		
	}
	
	@PostMapping
	public ResponseEntity<Produtos> postProdutos(@Valid @RequestBody Produtos produtos) {
		   if(categoriaRepository.existsById(produtos.getCategorias().getId()))
			   return ResponseEntity.status(HttpStatus.CREATED).body(produtosRepository.save(produtos));
	   return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PutMapping
	public ResponseEntity<Produtos> putProduto(@Valid @RequestBody Produtos produtos) {
		if (produtosRepository.existsById(produtos.getId())) {
			if (categoriaRepository.existsById(produtos.getCategorias().getId()))
                    return ResponseEntity.ok(produtosRepository.save(produtos));
                    else
                    	return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.notFound().build();	
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable Long id) {
		return produtosRepository.findById(id).map(resposta -> {
			produtosRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}).orElse(ResponseEntity.notFound().build());
		
	}
	
}
