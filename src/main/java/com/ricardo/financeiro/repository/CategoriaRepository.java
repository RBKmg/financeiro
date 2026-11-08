package com.ricardo.financeiro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Categoria;
import com.ricardo.financeiro.repository.helper.categoria.CategoriaQueries;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, CategoriaQueries {
	
	public Optional<Categoria> findByNomeIgnoreCase(String nome);
 
}
