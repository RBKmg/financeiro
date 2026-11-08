package com.ricardo.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.repository.helper.livro.LivroQueries;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>, LivroQueries {


}
