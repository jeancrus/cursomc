package com.jean.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jean.cursomc.domain.Cidade;
import com.jean.cursomc.domain.Cliente;
import com.jean.cursomc.domain.Endereco;
import com.jean.cursomc.domain.enums.TipoCliente;
import com.jean.cursomc.dto.ClienteDTO;
import com.jean.cursomc.dto.ClienteNewDTO;
import com.jean.cursomc.repositories.ClienteRepository;
import com.jean.cursomc.repositories.EnderecoRepository;
import com.jean.cursomc.services.exceptions.DataIntegrityException;
import com.jean.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id); // se existe retorna obj else retorna null
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Cliente não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null); //garante que o objeto inserido é novo(nulo), pois se não o insert é considerado como atualização e muda o obj ja criado
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); //procura o obj cliente.id e insere no newobj
		updateData(newObj, obj); //inserido para que não dê nullpointerexception na alteração de um cliente
		//update data foi inserido como novo método
		return repo.save(newObj); //alteração pelo banco
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
		
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null); //não instancia do banco de dados
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null,objDto.getLogradouro(), objDto.getNumero(), objDto.getNumero(), objDto.getBairro(), objDto.getCep(), cli , cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}// novo metodo inserido para que somente o email e nome sejam alterados
	
	
}
