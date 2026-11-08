package com.ricardo.financeiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {

}
