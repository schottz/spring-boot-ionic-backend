package com.felipeschottz.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipeschottz.cursomc.domain.ItemPedido;
import com.felipeschottz.cursomc.domain.PagamentoComBoleto;
import com.felipeschottz.cursomc.domain.Pedido;
import com.felipeschottz.cursomc.domain.enums.EstadoPagamento;
import com.felipeschottz.cursomc.repositories.ItemPedidoRepository;
import com.felipeschottz.cursomc.repositories.PagamentoRepository;
import com.felipeschottz.cursomc.repositories.PedidoRepository;
import com.felipeschottz.cursomc.repositories.ProdutoRepository;
import com.felipeschottz.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagRepo;
	
	@Autowired
	private ProdutoRepository prodRepo;
	
	@Autowired
	private ItemPedidoRepository ipRepo;

	public Pedido buscar(Integer id) {
		Pedido obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado " + id);
		}
		return obj;
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagRepo.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(prodRepo.findOne(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		ipRepo.save(obj.getItens());
		return obj;
	}
}
