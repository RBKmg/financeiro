package com.ricardo.financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.model.Cidade;
import com.ricardo.financeiro.repository.CidadeRepository;
import com.ricardo.financeiro.repository.EstadoRepository;
import com.ricardo.financeiro.repository.filter.CidadesFilter;
import com.ricardo.financeiro.service.CadastroCidadeService;
import com.ricardo.financeiro.service.exception.CidadeJaCadastradaException;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cidades")
public class CidadesController {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidadeService;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cidade cidade) {
		ModelAndView mv = new ModelAndView("cidades/CadastroCidade");
		mv.addObject("estados", estadoRepository.findAll());
		return mv;
	}
	
	@Cacheable(value = "cidades", key = "#codigoEstado")
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Cidade> pesquisarPorCodigoEstado(@RequestParam(name = "estado", defaultValue = "-1") Long codigoEstado) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		return cidadeRepository.findByEstadoCodigo(codigoEstado);
	}
	
	@PostMapping({"/novo", "/novo/{codigo}"})
	@CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()")
	public ModelAndView cadastrar(@Valid Cidade cidade, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(cidade);
		}
		
		try {
			cadastroCidadeService.cadastrar(cidade);
		} catch (CidadeJaCadastradaException exception) {
			result.rejectValue("nome", exception.getMessage(), exception.getMessage());
			return novo(cidade);
		}
		
		attributes.addFlashAttribute("mensagem", "Cidade cadastrada com sucesso!");
		return new ModelAndView("redirect:/cidades/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(CidadesFilter cidadesFilter, BindingResult result,
			@PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		
		ModelAndView mv = new ModelAndView("cidades/PesquisaCidades");
		mv.addObject("estados", estadoRepository.findAll());
		
		PageWrapper<Cidade> paginaWrapper = new PageWrapper<>(cidadeRepository.filtrar(cidadesFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView atualizar(@PathVariable("codigo") Cidade cidade) {
		ModelAndView mv = this.novo(cidade);
		mv.addObject(this.cidadeRepository.findByCodigoFetchingEstado(cidade.getCodigo()));
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> excluir(@PathVariable("codigo") Cidade cidade) {
		try {
			this.cadastroCidadeService.excluir(cidade);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}

}
