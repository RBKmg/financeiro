package com.ricardo.financeiro.repository.helper.lancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.dto.LancamentoAnualPorTipo;
import com.ricardo.financeiro.dto.LancamentoEstatisticaCategoria;
import com.ricardo.financeiro.dto.LancamentoEstatisticaDia;
import com.ricardo.financeiro.model.Lancamento;
import com.ricardo.financeiro.repository.filter.LancamentosFilter;

public interface LancamentoQueries {
	
	public Page<Lancamento> filtrar(LancamentosFilter filtro, Pageable pageable);
	
	// BOX
	public BigDecimal totalDeReceitaNoMes();
	public BigDecimal totalDeDespesaNoMes();
	
	// Dashboard
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	
	// Dashboard 2 -> ANUAL
	//public List<GraficoVendasPorMes> totalVendasPorMes();
	public List<LancamentoAnualPorTipo> totalAnualPorTipo(); //VendaOrigem

}
