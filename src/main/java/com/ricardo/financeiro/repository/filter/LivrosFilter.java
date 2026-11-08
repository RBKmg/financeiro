package com.ricardo.financeiro.repository.filter;

import java.math.BigDecimal;

import com.ricardo.financeiro.model.Origem;
import com.ricardo.financeiro.model.TipoLivro;

public class LivrosFilter {
	
	private String titulo;
	private String autor;
	private Origem origem;
	private TipoLivro tipo;
	private BigDecimal valorDe;
	private BigDecimal valorAte;
	
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public Origem getOrigem() {
		return origem;
	}
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	public TipoLivro getTipo() {
		return tipo;
	}
	public void setTipo(TipoLivro tipo) {
		this.tipo = tipo;
	}
	public BigDecimal getValorDe() {
		return valorDe;
	}
	public void setValorDe(BigDecimal valorDe) {
		this.valorDe = valorDe;
	}
	public BigDecimal getValorAte() {
		return valorAte;
	}
	public void setValorAte(BigDecimal valorAte) {
		this.valorAte = valorAte;
	}
	
}
