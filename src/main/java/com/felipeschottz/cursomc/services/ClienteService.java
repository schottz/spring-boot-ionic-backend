package com.felipeschottz.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipeschottz.cursomc.domain.Cliente;
import com.felipeschottz.cursomc.repositories.ClienteRepository;
import com.felipeschottz.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;

	public Cliente buscar(Integer id) {
		Cliente obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado " + id);
		}
		return obj;
	}
}
