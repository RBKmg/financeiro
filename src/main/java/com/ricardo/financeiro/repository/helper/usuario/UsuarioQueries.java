package com.ricardo.financeiro.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.dto.UsuarioDTO;
import com.ricardo.financeiro.model.Usuario;
import com.ricardo.financeiro.repository.filter.UsuariosFilter;

public interface UsuarioQueries {
	
	public Optional<Usuario> porEmailEAtivo(String email);	
	
	public List<String> permissoes(Usuario usuario);
	
	public Page<Usuario> filtrar(UsuariosFilter filtro, Pageable pageable);
	
	public List<UsuarioDTO> carregarPessoaUsuario();
	
	public Usuario buscarUsuarioComGrupos(Long codigo);

}
