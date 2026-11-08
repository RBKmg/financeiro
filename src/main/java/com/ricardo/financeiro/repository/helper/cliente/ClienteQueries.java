package com.ricardo.financeiro.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.model.Cliente;
import com.ricardo.financeiro.repository.filter.ClientesFilter;

public interface ClienteQueries {
	
	public Page<Cliente> filtrar(ClientesFilter clientesFilter, Pageable pageable);
	
	public Long totalDeClientes();

}
