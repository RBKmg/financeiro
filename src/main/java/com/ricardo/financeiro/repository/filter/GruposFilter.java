package com.ricardo.financeiro.repository.filter;

import java.util.List;

import com.ricardo.financeiro.model.Permissao;

public class GruposFilter {
	
	private Long codigo;
	private String nome;
	private List<Permissao> permissoes;
	
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Permissao> getPermissoes() {
		return permissoes;
	}
	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

}
