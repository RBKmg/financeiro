package com.ricardo.financeiro.repository.helper.venda;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.dto.GraficoVendasPorMes;
import com.ricardo.financeiro.dto.VendaOrigem;
import com.ricardo.financeiro.model.Venda;
import com.ricardo.financeiro.repository.filter.VendasFilter;

public interface VendaQueries {
	
	public Page<Venda> filtrar(VendasFilter filtro, Pageable pageable);
	
	public Venda buscarComItens(Long codigo);
	
	public BigDecimal valorTotalDuranteAno();
	public BigDecimal valorTotalDuranteMes();
	public BigDecimal valorTotalTicketMedio();
	
	public List<GraficoVendasPorMes> totalVendasPorMes();
	public List<VendaOrigem> totalPorOrigem();
	
}
