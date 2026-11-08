package com.ricardo.financeiro.service.event.livro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ricardo.financeiro.storage.FotoStorage;

@Component
public class LivroListener {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@EventListener(condition = "#evento.temFoto() and #evento.novaFoto")
	public void livroSalvo(LivroSalvoEvent evento) {
//		System.out.println("Novo livrio salvo " + evento.getLivro().getTitulo());
		fotoStorage.salvar(evento.getLivro().getFoto());
	}

}
