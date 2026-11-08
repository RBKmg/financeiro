package com.ricardo.financeiro.repository.helper.grupo;

import java.util.List;

import com.ricardo.financeiro.model.Grupo;
import com.ricardo.financeiro.repository.filter.GruposFilter;

public interface GrupoQueries {
	
	public List<Grupo> filtrar(GruposFilter filtro);
	
	public Grupo buscarGrupoComPermissoes(Long codigo);

}
