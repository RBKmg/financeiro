package com.ricardo.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Grupo;
import com.ricardo.financeiro.repository.helper.grupo.GrupoQueries;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long>, GrupoQueries {


}
