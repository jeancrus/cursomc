package com.jean.cursomc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jean.cursomc.domain.Pedido;
import com.jean.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.POST) //mapeado para que seja reconhecido no endpoint categorias
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) { //request body pois necessário para que o json seja reconhecido automaticamente para o java
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest() //Sendo feito para que a / após a categoria seja acrescentada o id novo inserido
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); //do banco de dados 
		return ResponseEntity.created(uri).build();
	}
}
