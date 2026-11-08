package com.ricardo.financeiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Grupo;
import com.ricardo.financeiro.repository.GrupoRepository;

@Service
public class CadastroGrupoService {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Transactional
	public void cadastrar(Grupo grupo) {
		Grupo grupoExistente = grupoRepository.getOne(grupo.getCodigo());
		grupo.setNome(grupoExistente.getNome());
		grupoRepository.save(grupo);
	}

}
