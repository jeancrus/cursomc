package com.jean.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jean.cursomc.domain.ItemPedido;
import com.jean.cursomc.domain.PagamentoComBoleto;
import com.jean.cursomc.domain.Pedido;
import com.jean.cursomc.domain.enums.EstadoPagamento;
import com.jean.cursomc.repositories.ItemPedidoRepository;
import com.jean.cursomc.repositories.PagamentoRepository;
import com.jean.cursomc.repositories.PedidoRepository;
import com.jean.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id); // se existe retorna obj else retorna null
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
		}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null); //garante que o objeto inserido é novo(nulo), pois se não o insert é considerado como atualização e muda o obj ja criado
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco()); //buscando o produto inteiro pelo id no banco e entao insere no itempedido
			ip.setPedido(obj); //associar o item pedido com pedido que estamos inserindo obj
		}
		itemPedidoRepository.saveAll(obj.getItens());
		
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
		
	}
	
}
