package com.ricardo.financeiro.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import com.ricardo.financeiro.model.ItemVenda;
import com.ricardo.financeiro.model.Livro;

public class TabelaItensVenda {
	
	private String uuid;
	private List<ItemVenda> itens = new ArrayList<>();
	
	
	public TabelaItensVenda(String uuid) {
		this.uuid = uuid;
	}

	
	public String getUuid() {
		return uuid;
	}
	
	public List<ItemVenda> getItens() {
		return itens;
	}
	

	public BigDecimal getValorTotal() {
		return itens.stream()
				.map(ItemVenda::getValorTotal)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public void adicionarItem(Livro livro, Integer quantidade) {
		Optional<ItemVenda> itensOptional = buscarItemDeAcordoComLivro(livro);
		
		ItemVenda itemVenda = null;
		if(itensOptional.isPresent()) {
			itemVenda = itensOptional.get();
			itemVenda.setQuantidade(itemVenda.getQuantidade() + quantidade);
		} else {
			itemVenda = new ItemVenda();
			itemVenda.setLivro(livro);
			itemVenda.setQuantidade(quantidade);
			itemVenda.setValorUnitario(livro.getValor());			
			itens.add(0, itemVenda);		
		}		
	}
	
	public void alterarQuantidadeDeItens(Livro livro, Integer quantidade) {
		ItemVenda itemVenda = buscarItemDeAcordoComLivro(livro).get();
		itemVenda.setQuantidade(quantidade);
	}
	
	public void excluirItemVendaDaTabela(Livro livro) {
		int indice = IntStream.range(0, itens.size())
				.filter(i -> itens.get(i).getLivro().equals(livro))
				.findAny().getAsInt();
		itens.remove(indice);
	}
	
	public int total() {
		return itens.size();
	}
	
	
	private Optional<ItemVenda> buscarItemDeAcordoComLivro(Livro livro) {
		return itens.stream()
			.filter(i -> i.getLivro().equals(livro))
			.findAny();
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabelaItensVenda other = (TabelaItensVenda) obj;
		return Objects.equals(uuid, other.uuid);
	}

}
