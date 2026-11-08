package com.ricardo.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricardo.financeiro.model.Usuario;
import com.ricardo.financeiro.repository.helper.usuario.UsuarioQueries;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioQueries {
	
	public Optional<Usuario> findByEmail(String email);
	
	public List<Usuario> findByCodigoIn(Long[] codigos);

}
