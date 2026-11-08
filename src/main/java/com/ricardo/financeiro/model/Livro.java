package com.ricardo.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.mysql.cj.util.StringUtils;
import com.ricardo.financeiro.validation.ISBN;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "livro")
public class Livro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotBlank(message = "Título não pode ficar em branco")
	@Size(max = 50)
	private String titulo;

	@NotBlank(message = "Autor não pode ficar em branco")
	@Size(max = 50)
	private String autor;

	@Size(min = 1, max = 255, message = "A descrição deve estar entre 1 e 255 caracteres")
	private String descricao;

	@NotNull(message = "Tipo não pode ficar nulo")
	@Enumerated(EnumType.STRING)
	private TipoLivro tipo;

	@NotNull(message = "Quantidade de páginas não pode ficar nulo")
	@Max(value = 9999)
	@Column(name = "quantidade_paginas")
	private Integer paginas;

	@ISBN(message = "Exatamente 13 caracteres")
	@NotBlank(message = "ISBN não pode ficar nulo")
	private String isbn;

	@NotNull(message = "Origem não pode ficar nulo")
	@Enumerated(EnumType.STRING)
	private Origem origem;

	@NotNull(message = "Valor não pode ficar nulo")
	@DecimalMin("0.01")
	@DecimalMax(value = "9999999.99")
	private BigDecimal valor;

	@NotNull(message = "Comissão não pode ficar nulo")
	@DecimalMax(value = "100.0")
	private BigDecimal comissao;

	@NotNull(message = "Quantidade do estoque não pode ficar nulo")
	@Max(value = 9999)
	@Column(name = "quantidade_estoque")
	private Integer estoque;
	
	private String foto;
	
	@Column(name = "content_type")
	private String contentType;
	
	@Transient
	private boolean novaFoto;
	
	
	public String getFotoOuMock() {
		return !StringUtils.isNullOrEmpty(foto) ? foto : "sem-foto-mock.png";
	}
	
	public boolean temFoto() {
		return !StringUtils.isNullOrEmpty(this.foto);
	}
	
	public boolean isNovo() {
		return codigo == null;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public TipoLivro getTipo() {
		return tipo;
	}
	public void setTipo(TipoLivro tipo) {
		this.tipo = tipo;
	}
	public Integer getPaginas() {
		return paginas;
	}
	public void setPaginas(Integer paginas) {
		this.paginas = paginas;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getComissao() {
		return comissao;
	}
	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}
	public Integer getEstoque() {
		return estoque;
	}
	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}
	public Origem getOrigem() {
		return origem;
	}
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}	
	public boolean isNovaFoto() {
		return novaFoto;
	}
	public void setNovaFoto(boolean novaFoto) {
		this.novaFoto = novaFoto;
	}


	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		return Objects.equals(codigo, other.codigo);
	}

}
