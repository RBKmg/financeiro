package com.ricardo.financeiro.model;

public enum TipoLivro {

	LIVRO("Livro"), LIVRETO("Livreto"), FOLHETO("Folheto");

	private String descricao;

	TipoLivro(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
