package com.ricardo.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ricardo.financeiro.repository.ClienteRepository;
import com.ricardo.financeiro.repository.LivroRepository;
import com.ricardo.financeiro.repository.VendaRepository;

@Controller
public class DashboardVendaLivroController {
	
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@RequestMapping("/dashboard")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("dashboard/DashboardVendaLivro");
		
		mv.addObject("valorTotalEstoque", livroRepository.valorTotalNoEstoque());
		mv.addObject("totalItensNoEstoque", livroRepository.totalItensNoEstoque());
		mv.addObject("totalDeClientes", clienteRepository.totalDeClientes());
		
		mv.addObject("vendasNoAno", vendaRepository.valorTotalDuranteAno());
		mv.addObject("vendasNoMes", vendaRepository.valorTotalDuranteMes());
		mv.addObject("valorTicketMedioNoAno", vendaRepository.valorTotalTicketMedio());
		return mv;
	}

}
