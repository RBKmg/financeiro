package com.ricardo.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.model.Categoria;
import com.ricardo.financeiro.repository.CategoriaRepository;
import com.ricardo.financeiro.repository.filter.CategoriasFilter;
import com.ricardo.financeiro.service.CadastroCategoriaService;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;
import com.ricardo.financeiro.service.exception.NomeDaCategoriaJaCadastradaException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categorias")
public class CategoriasController {

	@Autowired
	private CadastroCategoriaService cadastroCategoriaService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@RequestMapping("/novo")
	public ModelAndView novo(Categoria categoria) {
		ModelAndView mv = new ModelAndView("categorias/CadastroCategoria");
		return mv;
	}

	@RequestMapping(value = { "/novo", "/{codigo}" }, method = RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Categoria categoria, BindingResult result, Model model,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(categoria);
		}

		try {
			cadastroCategoriaService.salvar(categoria);
		} catch (NomeDaCategoriaJaCadastradaException exception) {
			result.rejectValue("nome", exception.getMessage(), exception.getMessage());
			return novo(categoria);
		}

		attributes.addFlashAttribute("mensagem", "Categoria salva com sucesso!");
		return new ModelAndView("redirect:/categorias/novo");
	}

	// @RequestMapping(method = RequestMethod.POST, consumes = {
	// MediaType.APPLICATION_JSON_VALUE })
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> salvar(@RequestBody @Valid Categoria categoria, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldError("nome").getDefaultMessage());
		}

		categoria = cadastroCategoriaService.salvar(categoria);
		return ResponseEntity.ok(categoria);
	}

	@GetMapping
	public ModelAndView pesquisar(CategoriasFilter categoriasFilter, BindingResult result,
			@PageableDefault(size = 5) Pageable pageable, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("categorias/PesquisaCategorias");

		PageWrapper<Categoria> paginaWrapper = new PageWrapper<>(
				categoriaRepository.filtrar(categoriasFilter, pageable), request);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	@DeleteMapping("/{codigo}")
	@ResponseBody
	public ResponseEntity<?> excluir(@PathVariable("codigo") Long codigo) {
		try {
			cadastroCategoriaService.excluir(codigo);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Long codigo) {
		Categoria categoria = categoriaRepository.findById(codigo).orElse(null);
		ModelAndView mv = novo(categoria);
		mv.addObject(categoria);
		return mv;
	}

}
