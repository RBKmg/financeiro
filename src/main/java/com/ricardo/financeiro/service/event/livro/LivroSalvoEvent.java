package com.ricardo.financeiro.service.event.livro;

import com.ricardo.financeiro.model.Livro;

public class LivroSalvoEvent {
	
	private Livro livro;

	public LivroSalvoEvent(Livro livro) {
		this.livro = livro;
	}

	public Livro getLivro() {
		return livro;
	}
	
	
	public boolean temFoto() {
	    return livro.getFoto() != null && !livro.getFoto().isBlank();
	}
	
	public boolean isNovaFoto() {
		return livro.isNovaFoto();
	}

}
