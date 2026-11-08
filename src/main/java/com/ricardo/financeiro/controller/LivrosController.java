package com.ricardo.financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.dto.LivroDTO;
import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.model.Origem;
import com.ricardo.financeiro.model.TipoLivro;
import com.ricardo.financeiro.repository.LivroRepository;
import com.ricardo.financeiro.repository.filter.LivrosFilter;
import com.ricardo.financeiro.service.CadastroLivroService;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/livros")
public class LivrosController {

	@Autowired
	private CadastroLivroService cadastroLivroService;
	
	@Autowired 
	private LivroRepository livroRepository;


	@RequestMapping("/novo")
	public ModelAndView novo(Livro livro) {
		ModelAndView mv = new ModelAndView("livros/CadastroLivro");
		mv.addObject("tipos", TipoLivro.values());
		mv.addObject("origens", Origem.values());
		return mv;
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Livro livro, BindingResult result, Model model, RedirectAttributes attributes) {
		
		// FORWARD
		if (result.hasErrors()) {
			//model.addAttribute(livro);
			return novo(livro);
		}

		// REDIRECT
		cadastroLivroService.salvar(livro);
		attributes.addFlashAttribute("mensagem", "Livro salvo com sucesso!");
		return new ModelAndView("redirect:/livros/novo");
	}

	@RequestMapping("/cervejas/cadastro")
	public String cadastro() {
		return "livros/cadastro-produto";
	}
	
	@GetMapping
	public ModelAndView pesquisar(LivrosFilter livrosFilter, BindingResult result, 
			@PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("livros/PesquisaLivros");
		mv.addObject("tipos", TipoLivro.values());
		mv.addObject("origens", Origem.values());

		PageWrapper<Livro> paginasWrapper = new PageWrapper<>(livroRepository.filtrar(livrosFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginasWrapper);
		return mv;
	}
	
	@GetMapping(value = "/filtro", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<LivroDTO> pesquisaLivroPorIsbnOuNome_CadastroVenda(String isbnOuTitulo) {
		return livroRepository.porIsbnOuTitulo(isbnOuTitulo);
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Livro livro) {
		try {
			cadastroLivroService.excluir(livro);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Livro livro) {
		ModelAndView mv = novo(livro);
		mv.addObject(livro);
		return mv;
	}


}
