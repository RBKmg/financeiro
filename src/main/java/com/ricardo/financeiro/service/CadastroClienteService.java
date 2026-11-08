package com.ricardo.financeiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Cidade;
import com.ricardo.financeiro.model.Cliente;
import com.ricardo.financeiro.repository.CidadeRepository;
import com.ricardo.financeiro.repository.ClienteRepository;
import com.ricardo.financeiro.service.exception.CpfOuCnpjClienteJaCadastradoException;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;

import jakarta.persistence.PersistenceException;

@Service
public class CadastroClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Transactional
	public void salvar(Cliente cliente) {
//		Optional<Cliente> clienteExistente = clienteRepository.findByCpfOuCnpj(cliente.getCPfOuCnpjSemFormatacao());		
//		if(clienteExistente.isPresent()) {
//			throw new CpfOuCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado!");
//		}
		if (cliente.isNovo()) {
			this.clienteRepository.findByCpfOuCnpj(cliente.getCPfOuCnpjSemFormatacao())
				.ifPresent(c -> {
					throw new CpfOuCnpjClienteJaCadastradoException("CPF/CNPJ já cadastrado"); 
					}
				);
		}
		
		clienteRepository.save(cliente);
	}
	
	@Transactional(readOnly = true)
	public void comporDadosEndereco(Cliente cliente) {
		if (cliente.getEndereco() == null 
				|| cliente.getEndereco().getCidade() == null
				|| cliente.getEndereco().getCidade().getCodigo() == null) {
			return;
		}

		Cidade cidade = this.cidadeRepository.findByCodigoFetchingEstado(cliente.getEndereco().getCidade().getCodigo());

		cliente.getEndereco().setCidade(cidade);
		cliente.getEndereco().setEstado(cidade.getEstado());
	}

	@Transactional
	public void excluir(Cliente cliente) {
		try {
			this.clienteRepository.delete(cliente);
			this.clienteRepository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar cliente. Já está atrelado a alguma venda.");
		}
	}

}
