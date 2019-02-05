package com.felipeschottz.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipeschottz.cursomc.domain.Cidade;
import com.felipeschottz.cursomc.domain.Cliente;
import com.felipeschottz.cursomc.domain.Endereco;
import com.felipeschottz.cursomc.domain.enums.TipoCliente;
import com.felipeschottz.cursomc.dto.ClienteDTO;
import com.felipeschottz.cursomc.dto.ClienteNewDTO;
import com.felipeschottz.cursomc.repositories.CidadeRepository;
import com.felipeschottz.cursomc.repositories.ClienteRepository;
import com.felipeschottz.cursomc.repositories.EnderecoRepository;
import com.felipeschottz.cursomc.services.exceptions.DataIntegrityException;
import com.felipeschottz.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private CidadeRepository cidadeRepo;
	
	@Autowired
	private EnderecoRepository endRepo;

	public Cliente find(Integer id) {
		Cliente obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado. ID: " + id);
		}
		return obj;
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		endRepo.save(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.delete(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir, pois ainda há entidades relacionadas.");
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String ordeBy, String direction){

		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), ordeBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO obj) {
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO obj) {
		Cliente cli = new Cliente(null, obj.getNome(), obj.getEmail(), obj.getCpfCnpj(), TipoCliente.toEnum(obj.getTipo()));
		Cidade cid = cidadeRepo.findOne(obj.getCidadeId());
		Endereco end = new Endereco(null, obj.getLogradouro(), obj.getNumero(), obj.getComplemento(), obj.getBairro(), obj.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(obj.getTelefone1());
		if(obj.getTelefone2() != null) {
			cli.getTelefones().add(obj.getTelefone2());
		}
		if(obj.getTelefone3() != null) {
			cli.getTelefones().add(obj.getTelefone3());
		}
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
