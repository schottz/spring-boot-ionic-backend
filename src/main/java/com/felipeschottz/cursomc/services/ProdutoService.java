package com.felipeschottz.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipeschottz.cursomc.domain.Categoria;
import com.felipeschottz.cursomc.domain.Produto;
import com.felipeschottz.cursomc.repositories.CategoriaRepository;
import com.felipeschottz.cursomc.repositories.ProdutoRepository;
import com.felipeschottz.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository catRepo;

	public Produto find(Integer id) {
		Produto obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado " + id);
		}
		return obj;
	}
	
	public Page<Produto> search(String nome, List<Integer> ids , Integer page, Integer linesPerPage, String ordeBy, String direction){

		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), ordeBy);
		List<Categoria> categorias = catRepo.findAll(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}
