package com.ricardo.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Cidade;
import com.ricardo.financeiro.model.Estado;
import com.ricardo.financeiro.repository.helper.cidade.CidadeQueries;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>, CidadeQueries {
	
	public List<Cidade> findByEstadoCodigo(Long codigoEstado);
	
	public Optional<Cidade> findByNomeAndEstado(String nome, Estado estado);
	
	@Query("select c from Cidade c join fetch c.estado where c.codigo = :codigo")
	public Cidade findByCodigoFetchingEstado(@Param("codigo") Long codigo);

}
