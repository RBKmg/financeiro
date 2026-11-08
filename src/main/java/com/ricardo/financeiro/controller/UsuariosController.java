package com.ricardo.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.model.Usuario;
import com.ricardo.financeiro.repository.GrupoRepository;
import com.ricardo.financeiro.repository.UsuarioRepository;
import com.ricardo.financeiro.repository.filter.UsuariosFilter;
import com.ricardo.financeiro.service.CadastroUsuarioService;
import com.ricardo.financeiro.service.StatusUsuario;
import com.ricardo.financeiro.service.exception.EmailDoUsuarioJaCadastradoException;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;
import com.ricardo.financeiro.service.exception.SenhaObrigatoriaUsuarioException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuarios/CadastroUsuario");
		mv.addObject("grupos", grupoRepository.findAll());
		return mv;
	}
	
	@PostMapping({ "/novo", "/{codigo}" })
	public ModelAndView cadastrarEatualizar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(usuario);
		}
		
		try {
			cadastroUsuarioService.cadastrar(usuario);
		} catch (EmailDoUsuarioJaCadastradoException ex) {
			result.rejectValue("email", ex.getMessage(), ex.getMessage());
			return novo(usuario);
		} catch (SenhaObrigatoriaUsuarioException ex) {
			result.rejectValue("senha", ex.getMessage(), ex.getMessage());
			return novo(usuario);
		}
		
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
		return new ModelAndView("redirect:/usuarios/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(UsuariosFilter usuariosFilter, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("usuarios/PesquisaUsuarios");		
		mv.addObject("grupos", grupoRepository.findAll());
		PageWrapper<Usuario> paginasWrapper = new PageWrapper<>(usuarioRepository.filtrar(usuariosFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginasWrapper);
		return mv;
	}
	
	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos, @RequestParam("status") StatusUsuario statusUsuario) {
		cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Usuario usuario = usuarioRepository.buscarUsuarioComGrupos(codigo);
		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Usuario usuario) {
		try {
			cadastroUsuarioService.excluir(usuario);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.ok().build();
	}

}
