package com.ricardo.financeiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Categoria;
import com.ricardo.financeiro.repository.CategoriaRepository;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;
import com.ricardo.financeiro.service.exception.NomeDaCategoriaJaCadastradaException;

import jakarta.persistence.PersistenceException;

@Service
public class CadastroCategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Transactional
	public Categoria salvar(Categoria categoria) {
		Optional<Categoria> categoriaOptional = categoriaRepository.findByNomeIgnoreCase(categoria.getNome());
		if(categoriaOptional.isPresent() && categoria.isNova()) {
			throw new NomeDaCategoriaJaCadastradaException("Nome da categoria já cadastrada!");
		}
		return categoriaRepository.save(categoria);
	}

	@Transactional
	public void excluir(Long codigo) {
		try {
			categoriaRepository.deleteById(codigo);
			categoriaRepository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar categoria já foi usada em algum lançamento");
		}
	}

}
