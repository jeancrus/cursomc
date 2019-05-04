package com.jean.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.jean.cursomc.domain.Categoria;
import com.jean.cursomc.dto.CategoriaDTO;
import com.jean.cursomc.repositories.CategoriaRepository;
import com.jean.cursomc.resources.exception.FieldMessage;

public class CategoriaInsertValidator implements ConstraintValidator<CategoriaInsert, CategoriaDTO> {
	
	@Autowired
	private CategoriaRepository repo;
	
	@Override
	public void initialize(CategoriaInsert ann) {
	}

	@Override
	public boolean isValid(CategoriaDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		Categoria aux = repo.findByNome(objDto.getNome());
		if (aux != null) {
			list.add(new FieldMessage("nome", "Categoria já cadastrada"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}