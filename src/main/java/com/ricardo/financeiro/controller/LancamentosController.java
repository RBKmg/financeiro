package com.ricardo.financeiro.controller;

import java.time.LocalDate;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.dto.LancamentoEstatisticaCategoria;
import com.ricardo.financeiro.dto.LancamentoEstatisticaDia;
import com.ricardo.financeiro.model.Lancamento;
import com.ricardo.financeiro.model.TipoLancamento;
import com.ricardo.financeiro.repository.CategoriaRepository;
import com.ricardo.financeiro.repository.LancamentoRepository;
import com.ricardo.financeiro.repository.UsuarioRepository;
import com.ricardo.financeiro.repository.filter.LancamentosFilter;
import com.ricardo.financeiro.service.CadastroLancamentoService;
import com.ricardo.financeiro.service.exception.UsuarioInexistenteOuInativoException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/lancamentos")
public class LancamentosController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CadastroLancamentoService cadastroLancamentoService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@GetMapping("/novo")
	public ModelAndView novo(Lancamento lancamento) {
		ModelAndView mv = new ModelAndView("lancamentos/CadastroLancamento");
		mv.addObject("tipos", TipoLancamento.values());
		mv.addObject("categorias", categoriaRepository.findAll());
		mv.addObject("pessoas", usuarioRepository.carregarPessoaUsuario());
		return mv;
	}
	
	@RequestMapping(value = { "/novo", "/{codigo}" }, method = RequestMethod.POST)
	public ModelAndView cadastrarEAtualizar(@Valid Lancamento lancamento, BindingResult result, Model model, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(lancamento);
		}
		
		try {
			cadastroLancamentoService.salvar(lancamento);
		} catch (UsuarioInexistenteOuInativoException ex) {
			result.rejectValue("usuario", ex.getMessage(), ex.getMessage());
			return novo(lancamento);
		}		
		
		attributes.addFlashAttribute("mensagem", "Lançamento salvo com sucesso");
		return new ModelAndView("redirect:/lancamentos/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(LancamentosFilter lancamentosFilter, @PageableDefault(size = 20) Pageable pageable, HttpServletRequest httpServletRequest) {		 
		ModelAndView mv = new ModelAndView("lancamentos/PesquisaLancamentos");
		
		PageWrapper<Lancamento> paginaWrapper = new PageWrapper<>(lancamentoRepository.filtrar(lancamentosFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Lancamento lancamento) {
		cadastroLancamentoService.excluir(lancamento);		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Lancamento lancamento) {
		ModelAndView mv = novo(lancamento);
		mv.addObject(lancamento);
		return mv;
	}
	
	@GetMapping("/por-categoria")
	public @ResponseBody List<LancamentoEstatisticaCategoria> porCategoria() {
		return lancamentoRepository.porCategoria(LocalDate.now());
	}
	
	@GetMapping("/por-dia")
	public @ResponseBody List<LancamentoEstatisticaDia> porDia() {
		return this.lancamentoRepository.porDia(LocalDate.now());
	}


}
