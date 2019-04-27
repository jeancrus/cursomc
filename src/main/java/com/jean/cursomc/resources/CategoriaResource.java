package com.jean.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jean.cursomc.domain.Categoria;
import com.jean.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Categoria obj = service.buscar(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.POST) //mapeado para que seja reconhecido no endpoint categorias
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) { //request body pois necessário para que o json seja reconhecido automaticamente para o java
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest() //Sendo feito para que a / após a categoria seja acrescentada o id novo inserido
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); //do banco de dados 
		return ResponseEntity.created(uri).build();
	}
}
