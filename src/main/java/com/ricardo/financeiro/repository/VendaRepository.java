package com.ricardo.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Venda;
import com.ricardo.financeiro.repository.helper.venda.VendaQueries;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>, VendaQueries {

}
