package com.ricardo.financeiro.repository.helper.cidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.model.Cidade;
import com.ricardo.financeiro.repository.filter.CidadesFilter;

public interface CidadeQueries {
	
	public Page<Cidade> filtrar(CidadesFilter cidadesFilter, Pageable pageable);

}
