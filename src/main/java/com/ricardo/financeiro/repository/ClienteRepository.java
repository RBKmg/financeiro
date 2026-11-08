package com.ricardo.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Cliente;
import com.ricardo.financeiro.repository.helper.cliente.ClienteQueries;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteQueries {
	
	public Optional<Cliente> findByCpfOuCnpj(String cpfOuCnpj);

	public List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

}
