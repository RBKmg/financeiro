package com.ricardo.financeiro.session;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ricardo.financeiro.model.Livro;

public class TabelaItensVendaTest {
	
	private TabelaItensVenda tabelaItensVenda;
	
	@BeforeEach
	public void Setup() {
		this.tabelaItensVenda = new TabelaItensVenda("1");
	}
	
	@Test
	public void deveCalcularValorTotalSemItens() throws Exception {
		assertEquals(BigDecimal.ZERO, tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveCalcularValorTotalComUmItem() throws Exception {
		Livro livro = new Livro();
		BigDecimal valor = new BigDecimal("8.90");
		livro.setValor(valor);
		
		tabelaItensVenda.adicionarItem(livro, 1);
		
		assertEquals(valor, tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveCalcularValorTotalComVariosItens() throws Exception {
		Livro l1 = new Livro();
		l1.setCodigo(1L);
		BigDecimal v1 = new BigDecimal("8.90");
		l1.setValor(v1);
		
		Livro l2 = new Livro();
		l2.setCodigo(2L);
		BigDecimal v2 = new BigDecimal("4.99");
		l2.setValor(v2);
		
		tabelaItensVenda.adicionarItem(l1, 1);
		tabelaItensVenda.adicionarItem(l2, 2);
		
		assertEquals(new BigDecimal("18.88"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveIncrementarQuantidadeDeLivrosQuandoSaoIguais() throws Exception {
		Livro l1 = new Livro();
		l1.setCodigo(1L);
		l1.setValor(new BigDecimal("4.50"));
		
		tabelaItensVenda.adicionarItem(l1, 1);
		tabelaItensVenda.adicionarItem(l1, 1);
		
		assertEquals(1, tabelaItensVenda.total());
		assertEquals(new BigDecimal("9.00"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveAlterarQuantidadeDItem() throws Exception {
		Livro l1 = new Livro();
		l1.setCodigo(1L);
		l1.setValor(new BigDecimal("4.50"));
		
		tabelaItensVenda.adicionarItem(l1, 1);
		tabelaItensVenda.alterarQuantidadeDeItens(l1, 3);
		
		assertEquals(1, tabelaItensVenda.total());
		assertEquals(new BigDecimal("13.50"), tabelaItensVenda.getValorTotal());
	}
	
	@Test
	public void deveExcluirItem() throws Exception {
		Livro l1 = new Livro();
		l1.setCodigo(1L);
		BigDecimal v1 = new BigDecimal("8.90");
		l1.setValor(v1);
		
		Livro l2 = new Livro();
		l2.setCodigo(2L);
		BigDecimal v2 = new BigDecimal("4.99");
		l2.setValor(v2);
		
		Livro l3 = new Livro();
		l3.setCodigo(3L);
		BigDecimal v3 = new BigDecimal("2.00");
		l3.setValor(v3);
		
		tabelaItensVenda.adicionarItem(l1, 1);
		tabelaItensVenda.adicionarItem(l2, 2);
		tabelaItensVenda.adicionarItem(l3, 1);
		
		tabelaItensVenda.excluirItemVendaDaTabela(l2);
		
		assertEquals(2, tabelaItensVenda.total());
		assertEquals(new BigDecimal("10.90"), tabelaItensVenda.getValorTotal());
	}

}
