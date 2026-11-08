package com.ricardo.financeiro.repository.helper.categoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.model.Categoria;
import com.ricardo.financeiro.repository.filter.CategoriasFilter;

public interface CategoriaQueries {
	
	public Page<Categoria> filtrar(CategoriasFilter categoriasFilter, Pageable pageable);

}
