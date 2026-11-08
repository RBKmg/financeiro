package com.ricardo.financeiro.dto;

import java.math.BigDecimal;

public class LancamentoAnualPorTipo {
	
	private String mes;
	private BigDecimal totalReceita;
	private BigDecimal totalDespesa;
	
	
	public LancamentoAnualPorTipo() {}
	
	public LancamentoAnualPorTipo(String mes, BigDecimal totalReceita, BigDecimal totalDespesa) {
		this.mes = mes;
		this.totalReceita = totalReceita;
		this.totalDespesa = totalDespesa;
	}
	

	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public BigDecimal getTotalReceita() {
		return totalReceita;
	}
	public void setTotalReceita(BigDecimal totalReceita) {
		this.totalReceita = totalReceita;
	}
	public BigDecimal getTotalDespesa() {
		return totalDespesa;
	}
	public void setTotalDespesa(BigDecimal totalDespesa) {
		this.totalDespesa = totalDespesa;
	}

}
