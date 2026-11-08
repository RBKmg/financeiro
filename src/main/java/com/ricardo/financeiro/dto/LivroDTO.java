package com.ricardo.financeiro.dto;

import java.math.BigDecimal;

import com.ricardo.financeiro.model.Origem;

public class LivroDTO {
	
	private Long codigo;
	private String isbn;
	private String titulo;
	private String origem;
	private BigDecimal valor;
	private String foto;	
	
	public LivroDTO(Long codigo, String isbn, String titulo, Origem origem, BigDecimal valor, String foto) {
		this.codigo = codigo;
		this.isbn = isbn;
		this.titulo = titulo;
		this.origem = origem.getDescricao();
		this.valor = valor;
		this.foto = (foto == null || foto.isBlank()) ? "sem-foto-mock.png" : foto;
	}

	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
}
