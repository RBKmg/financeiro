package com.ricardo.financeiro.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.repository.LivroRepository;
import com.ricardo.financeiro.service.event.livro.LivroSalvoEvent;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;
import com.ricardo.financeiro.storage.FotoStorage;

import jakarta.persistence.PersistenceException;

@Service
public class CadastroLivroService {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Transactional
	public void salvar(Livro livro) {
		livroRepository.save(livro);
	
		publisher.publishEvent(new LivroSalvoEvent(livro));
	}
	
	@Transactional
	public void excluir(Livro livro) {
		try {
			String foto = livro.getFoto();
			livroRepository.delete(livro);
			livroRepository.flush();
			fotoStorage.excluir(foto);
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar livro já foi usada em alguma venda");
		}		
	}

}
