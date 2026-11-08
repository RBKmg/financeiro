package com.ricardo.financeiro.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricardo.financeiro.controller.page.PageWrapper;
import com.ricardo.financeiro.controller.validator.VendaValidator;
import com.ricardo.financeiro.dto.GraficoVendasPorMes;
import com.ricardo.financeiro.dto.VendaOrigem;
import com.ricardo.financeiro.mail.Mailer;
import com.ricardo.financeiro.model.ItemVenda;
import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.model.StatusVenda;
import com.ricardo.financeiro.model.Venda;
import com.ricardo.financeiro.repository.LivroRepository;
import com.ricardo.financeiro.repository.VendaRepository;
import com.ricardo.financeiro.repository.filter.VendasFilter;
import com.ricardo.financeiro.security.UsuarioSistema;
import com.ricardo.financeiro.service.CadastroVendaService;
import com.ricardo.financeiro.session.TabelaItensSession;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private TabelaItensSession tabelaItensSession;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@Autowired
	private Mailer mailer;
	
	@InitBinder("venda")
	public void initializerValidator(WebDataBinder binder) {
		binder.setValidator(vendaValidator);
	}
	
	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) { // @InitBinder("venda") = venda
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		setUuid(venda);
		
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens", tabelaItensSession.getValorTotal(venda.getUuid()));
		
		return mv;
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendasFilter vendasFilter, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("venda/PesquisaVenda");
		mv.addObject("vendaStatus", StatusVenda.values());
		
		PageWrapper<Venda> paginaWrapper = new PageWrapper<>(vendaRepository.filtrar(vendasFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	
	  @GetMapping("/item")	  
	  @ResponseBody 
	  public ModelAndView adicionarItem(Long codigoLivro, String uuid) { 
		  Livro livro = livroRepository.findById(codigoLivro)
				  .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
		  tabelaItensSession.adicionarItem(livro, 1, uuid);	  
		  return mvtabelaItensSession(uuid); 
	  }
	
	@GetMapping("/item/{codigoLivro}")
	public ModelAndView alterarQuantidadeItem(@PathVariable Long codigoLivro, Integer quantidade, String uuid) {
		Livro livro = livroRepository.findById(codigoLivro)
				.orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));;
		tabelaItensSession.alterarQuantidadeDeItens(livro, quantidade, uuid);
		
		return mvtabelaItensSession(uuid);
	}
	
	@GetMapping("/item/{uuid}/{codigoLivro}")
	public ModelAndView exluirItemVenda(@PathVariable("codigoLivro") Livro livro, @PathVariable String uuid) {
		tabelaItensSession.excluirItemVendaDaTabela(livro, uuid);
		
		return mvtabelaItensSession(uuid);
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Venda venda = vendaRepository.buscarComItens(codigo);
		
		setUuid(venda);
		for(ItemVenda item : venda.getItens()) {
			tabelaItensSession.adicionarItem(item.getLivro(), item.getQuantidade(), venda.getUuid());
		}
		
		ModelAndView mv = nova(venda);
		mv.addObject(venda);
		return mv;
	}
	
	@GetMapping("/totalPorMes")
	public @ResponseBody List<GraficoVendasPorMes> listarTotalVendaPorMes() {
		return vendaRepository.totalVendasPorMes();
	}
	
	@GetMapping("/porOrigem")
	public @ResponseBody List<VendaOrigem> vendasPorNacionalidade() {
		return this.vendaRepository.totalPorOrigem();
	}
	
	@PostMapping(value = "/nova", params = "cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		try {
			cadastroVendaService.cancelar(venda);
		} catch(org.springframework.security.access.AccessDeniedException e) {
			return new ModelAndView("/403");
		}
		
		attributes.addFlashAttribute("mensagem", "Venda de código " + venda.getCodigo() + " foi cancelada");
		return new ModelAndView("redirect:/vendas/" + venda.getCodigo());		
	}
	
	@PostMapping(value = "/nova", params = "salvar")
	public ModelAndView salvar(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);		
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda realizada com sucesso!");
		return new ModelAndView("redirect:/vendas/nova");
	}	
	
	@PostMapping(value = "/nova", params = "salvarEmitir")
	public ModelAndView salvarEmitir(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);		
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		cadastroVendaService.salvarEmitir(venda);
		attributes.addFlashAttribute("mensagem", "Venda realizada e emitida com sucesso!");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params = "salvarEnviarEmail")
	public ModelAndView salvarEnviarEmail(Venda venda, BindingResult result, RedirectAttributes attributes, 
			@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, result);		
		if(result.hasErrors()) {
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		venda = cadastroVendaService.salvar(venda);
		mailer.enviar(venda);
		
		attributes.addFlashAttribute("mensagem", String.format("Venda nº %d realizada e e-mail enviado!", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}
	

	private ModelAndView mvtabelaItensSession(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaDeItensDeVenda");
		mv.addObject("itens", tabelaItensSession.getItens(uuid));
		mv.addObject("valorTotal", tabelaItensSession.getValorTotal(uuid));
		mv.addObject("venda", new Venda());
		
		return mv;
	}
	
	private void validarVenda(Venda venda, BindingResult result) {
		venda.adicionarItens(tabelaItensSession.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, result);
	}
	
	private void setUuid(Venda venda) {
		if(venda.getUuid() == null || venda.getUuid().isEmpty()) {
			venda.setUuid(UUID.randomUUID().toString());
		}
	}

}
