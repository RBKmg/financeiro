package com.ricardo.financeiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Cidade;
import com.ricardo.financeiro.repository.CidadeRepository;
import com.ricardo.financeiro.service.exception.CidadeJaCadastradaException;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;

import jakarta.persistence.PersistenceException;

@Service
public class CadastroCidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Transactional
	public void cadastrar(Cidade cidade) {
		Optional<Cidade> cidadeExistente = cidadeRepository.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
		if(cidadeExistente.isPresent()) {
			throw new CidadeJaCadastradaException("Cidade já está cadastrada");
		}
		cidadeRepository.save(cidade);
	}
	
	@Transactional
	public void excluir(Cidade cidade) {
		try {
			cidadeRepository.delete(cidade);
			cidadeRepository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cidade que já foi usada para um cliente");
		}
	}	

}
