package com.ricardo.financeiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ricardo.financeiro.model.Usuario;
import com.ricardo.financeiro.repository.UsuarioRepository;
import com.ricardo.financeiro.service.exception.EmailDoUsuarioJaCadastradoException;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;
import com.ricardo.financeiro.service.exception.SenhaObrigatoriaUsuarioException;

import jakarta.persistence.PersistenceException;

@Service
public class CadastroUsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public void cadastrar(Usuario usuario) {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new EmailDoUsuarioJaCadastradoException("E-mail já cadastrado");
		}
		
		if(usuario.isNovo() && !StringUtils.hasText(usuario.getSenha())) {
			throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
		}
		
		if(usuario.isNovo() || StringUtils.hasText(usuario.getSenha())) {
			usuario.setSenha(this.passwordEncoder.encode(usuario.getSenha()));
		} else if(!StringUtils.hasText(usuario.getSenha())) {
			usuario.setSenha(usuarioExistente.get().getSenha());
		}		
		usuario.setConfirmacaoSenha(usuario.getSenha());
		
		if(!usuario.isNovo() && usuario.getAtivo() == null) {
			usuario.setAtivo(usuarioExistente.get().getAtivo());
		}
		
		usuarioRepository.save(usuario);
	}

	@Transactional
	public void alterarStatus(Long[] codigos, StatusUsuario statusUsuario) {
		statusUsuario.executar(codigos, usuarioRepository);					
	}
	
	@Transactional
	public void excluir(Usuario usuario) {
		try {
			usuarioRepository.delete(usuario);
			usuarioRepository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException("Impossível apagar usuário está associado a algum lançamento");
		}		
	}

}
