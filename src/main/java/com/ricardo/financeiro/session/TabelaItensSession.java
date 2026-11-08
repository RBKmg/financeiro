package com.ricardo.financeiro.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.ricardo.financeiro.model.ItemVenda;
import com.ricardo.financeiro.model.Livro;

@SessionScope
@Component
public class TabelaItensSession {
	
	private Set<TabelaItensVenda> tabelas = new HashSet<>();


	public void adicionarItem(Livro livro, int quantidade, String uuid) {
		TabelaItensVenda tabela = buscarTabelaPorUUID(uuid);		
		tabela.adicionarItem(livro, quantidade);
		tabelas.add(tabela);
	}


	public void alterarQuantidadeDeItens(Livro livro, Integer quantidade, String uuid) {
		TabelaItensVenda tabela = buscarTabelaPorUUID(uuid);		
		tabela.alterarQuantidadeDeItens(livro, quantidade);
	}

	public void excluirItemVendaDaTabela(Livro livro, String uuid) {
		TabelaItensVenda tabela = buscarTabelaPorUUID(uuid);		
		tabela.excluirItemVendaDaTabela(livro);		
	}

	public List<ItemVenda> getItens(String uuid) {
		return buscarTabelaPorUUID(uuid).getItens();
	}
	
	public Object getValorTotal(String uuid) {		
		return buscarTabelaPorUUID(uuid).getValorTotal();
	}

	private TabelaItensVenda buscarTabelaPorUUID(String uuid) {
		TabelaItensVenda tabela = tabelas.stream()
				.filter(t -> t.getUuid().equals(uuid))
				.findAny()
				.orElse(new TabelaItensVenda(uuid));
		
		return tabela;
	}

}
