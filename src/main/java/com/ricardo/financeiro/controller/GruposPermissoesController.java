package com.ricardo.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.model.Grupo;
import com.ricardo.financeiro.repository.GrupoRepository;
import com.ricardo.financeiro.repository.PermissaoRepository;
import com.ricardo.financeiro.repository.filter.GruposFilter;
import com.ricardo.financeiro.service.CadastroGrupoService;

@Controller
@RequestMapping("/grupos")
public class GruposPermissoesController {
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@Autowired
	private CadastroGrupoService cadastroGrupoService;
	
	
	@GetMapping
	public ModelAndView pesquisar(GruposFilter filtro) {
		ModelAndView mv = new ModelAndView("grupo_permissao/PesquisaGruposPermissoes");
		mv.addObject("grupos", grupoRepository.filtrar(filtro));
		mv.addObject("permissoes", permissaoRepository.findAll());		
		return mv;
	}
	
	@RequestMapping("/novo")
	public ModelAndView novo(Grupo grupo) {
		ModelAndView mv = new ModelAndView("grupo_permissao/AssociarGrupoPermissoes");
		mv.addObject("permissoes", permissaoRepository.findAll());	
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Grupo grupo = grupoRepository.buscarGrupoComPermissoes(codigo);
		ModelAndView mv = novo(grupo);
		mv.addObject(grupo);
		return mv;
	}
	
	@PostMapping({ "/{codigo}" })
	public ModelAndView associar(Grupo grupo, BindingResult result, RedirectAttributes attributes) {
		
		cadastroGrupoService.cadastrar(grupo);
		
		attributes.addFlashAttribute("mensagem", "Permissões associadas com sucesso");
		return new ModelAndView("redirect:/grupos/novo");
	}

}
