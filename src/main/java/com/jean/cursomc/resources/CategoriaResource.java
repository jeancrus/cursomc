package com.jean.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jean.cursomc.domain.Categoria;
import com.jean.cursomc.dto.CategoriaDTO;
import com.jean.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Categoria obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.POST) //mapeado para que seja reconhecido no endpoint categorias
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) { //request body pois necessário para que o json seja reconhecido automaticamente para o java
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest() //Sendo feito para que a / após a categoria seja acrescentada o id novo inserido
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); //do banco de dados 
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT) //atualizar
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id) {
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll(); //buscando as listas do banco para converter para dto
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		/*ListDto == funcão para converter uma lista em outra lista!
		 * através do método stream.map coloca uma função nova que recebe obj como parametro
		 * e finaliza voltando essa função como lista através do collect Collectors.toList
		 */
		return ResponseEntity.ok().body(listDto);
	}
	
	/*
	 * Metodo utilizado para limitar paginação
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction); //buscando as listas do banco para converter para dto
		Page<CategoriaDTO> listDto = list.map(obj -> new CategoriaDTO(obj)); //por ser java 8, o page não precisa de collectors e stream
		return ResponseEntity.ok().body(listDto);
	}
}
