package com.ricardo.financeiro.service.event.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ricardo.financeiro.model.ItemVenda;
import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.repository.LivroRepository;

@Component
public class VendaListener {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent) {
		for (ItemVenda item : vendaEvent.getVenda().getItens()) {
			Livro livro = livroRepository.findById(item.getLivro().getCodigo())
					.orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
			
			livro.setEstoque(livro.getEstoque() - item.getQuantidade());
			livroRepository.save(livro);
		}
	}

}
