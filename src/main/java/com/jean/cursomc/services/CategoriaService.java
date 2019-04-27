package com.jean.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jean.cursomc.domain.Categoria;
import com.jean.cursomc.repositories.CategoriaRepository;
import com.jean.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id); // se existe retorna obj else retorna null
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
		}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null); //garante que o objeto inserido é novo(nulo), pois se não o insert é considerado como atualização e muda o obj ja criado
		return repo.save(obj);
	}
	
}
