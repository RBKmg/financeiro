package com.ricardo.financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.model.Cliente;
import com.ricardo.financeiro.model.TipoPessoa;
import com.ricardo.financeiro.repository.ClienteRepository;
import com.ricardo.financeiro.repository.EstadoRepository;
import com.ricardo.financeiro.repository.filter.ClientesFilter;
import com.ricardo.financeiro.service.CadastroClienteService;
import com.ricardo.financeiro.service.exception.CpfOuCnpjClienteJaCadastradoException;
import com.ricardo.financeiro.service.exception.ImpossivelExcluirEntidadeException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroClienteService cadastroClienteService;
	
	@RequestMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("clientes/CadastroCliente");
		mv.addObject("tiposPessoa", TipoPessoa.values());
		mv.addObject("estados", estadoRepository.findAll());
		return mv;
	}
	
	@PostMapping({"/novo", "/{codigo}"})
	public ModelAndView cadastrar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return novo(cliente);
		}
		
		try {
			cadastroClienteService.salvar(cliente);
		} catch (CpfOuCnpjClienteJaCadastradoException ex) {
			result.rejectValue("cpfOuCnpj", ex.getMessage(), ex.getMessage());
			return novo(cliente);
		}
		
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso");
		return new ModelAndView("redirect:/clientes/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClientesFilter clientesFilter, BindingResult result, 
			@PageableDefault(size = 5) Pageable pageable, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("clientes/PesquisaClientes");
		
		PageWrapper<Cliente> paginaWrapper = new PageWrapper<>(clienteRepository.filtrar(clientesFilter, pageable), request);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@RequestMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public List<Cliente> pesquisaRapida(String nome) {
		validarQuantidadeDeCaracteres(nome);
		return clienteRepository.findByNomeStartingWithIgnoreCase(nome);		
	}

	private void validarQuantidadeDeCaracteres(String nome) {
		if(nome == null || nome.isBlank() || nome.length() < 3) {
			throw new IllegalArgumentException();
		}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Cliente cliente) {
		ModelAndView mv = this.novo(cliente);
		this.cadastroClienteService.comporDadosEndereco(cliente);
		mv.addObject(cliente);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<?> excluir(@PathVariable("codigo") Cliente cliente) {
		try {
			this.cadastroClienteService.excluir(cliente);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}

}
