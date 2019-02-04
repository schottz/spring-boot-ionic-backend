package com.felipeschottz.cursomc;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.felipeschottz.cursomc.domain.Categoria;
import com.felipeschottz.cursomc.domain.Cidade;
import com.felipeschottz.cursomc.domain.Cliente;
import com.felipeschottz.cursomc.domain.Endereco;
import com.felipeschottz.cursomc.domain.Estado;
import com.felipeschottz.cursomc.domain.ItemPedido;
import com.felipeschottz.cursomc.domain.Pagamento;
import com.felipeschottz.cursomc.domain.PagamentoComBoleto;
import com.felipeschottz.cursomc.domain.PagamentoComCartao;
import com.felipeschottz.cursomc.domain.Pedido;
import com.felipeschottz.cursomc.domain.Produto;
import com.felipeschottz.cursomc.domain.enums.EstadoPagamento;
import com.felipeschottz.cursomc.domain.enums.TipoCliente;
import com.felipeschottz.cursomc.repositories.CategoriaRepository;
import com.felipeschottz.cursomc.repositories.CidadeRepository;
import com.felipeschottz.cursomc.repositories.ClienteRepository;
import com.felipeschottz.cursomc.repositories.EnderecoRepository;
import com.felipeschottz.cursomc.repositories.EstadoRepository;
import com.felipeschottz.cursomc.repositories.ItemPedidoRepository;
import com.felipeschottz.cursomc.repositories.PagamentoRepository;
import com.felipeschottz.cursomc.repositories.PedidoRepository;
import com.felipeschottz.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository catRepo;
	@Autowired
	private ProdutoRepository prodRepo;
	@Autowired
	private EstadoRepository estRepo;
	@Autowired
	private CidadeRepository cidRepo;
	@Autowired
	private ClienteRepository cliRepo;
	@Autowired
	private EnderecoRepository endRepo;
	@Autowired
	private PedidoRepository pedRepo;
	@Autowired
	private PagamentoRepository pagRepo;
	@Autowired
	private ItemPedidoRepository ipRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000.0);
		Produto p2 = new Produto(null, "Impressora", 800.0);
		Produto p3 = new Produto(null, "Mouse", 80.0);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().add(p2);
		
		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().add(cat1);
		
		catRepo.save(Arrays.asList(cat1, cat2));
		prodRepo.save(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().add(c1);
		est2.getCidades().addAll(Arrays.asList(c2, c3));
		
		estRepo.save(Arrays.asList(est1, est2));
		cidRepo.save(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "12926615779", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("121221", "2308930"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "200", "Apto 303", "Jardim", "289178", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "200", "Apto 303", "Jardim", "289178", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		cliRepo.save(Arrays.asList(cli1));
		endRepo.save(Arrays.asList(e1, e2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 10:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedRepo.save(Arrays.asList(ped1,ped2));
		pagRepo.save(Arrays.asList(pagto1,pagto2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.0);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.0);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.0);
		
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().add(ip3);
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		ipRepo.save(Arrays.asList(ip1,ip2,ip3));
		
	}

}