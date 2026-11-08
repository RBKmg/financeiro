package com.ricardo.financeiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Lancamento;
import com.ricardo.financeiro.repository.LancamentoRepository;
import com.ricardo.financeiro.service.exception.UsuarioInexistenteOuInativoException;

@Service
public class CadastroLancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Transactional
	public void salvar(Lancamento lancamento) {
		
		if(lancamento.getUsuario().isInativo()) {
			throw new UsuarioInexistenteOuInativoException("Usuário inativo para realizar cadastro de lançamento");
		}
		
		lancamentoRepository.save(lancamento);
	}
	
	@Transactional
	public void excluir(Lancamento lancamento) {		
		lancamentoRepository.delete(lancamento);
		lancamentoRepository.flush();				
	}

}
