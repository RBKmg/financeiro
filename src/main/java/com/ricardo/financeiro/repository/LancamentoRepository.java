package com.ricardo.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Lancamento;
import com.ricardo.financeiro.repository.helper.lancamento.LancamentoQueries;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoQueries {

}
