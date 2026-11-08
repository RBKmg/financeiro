package com.ricardo.financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ricardo.financeiro.dto.LancamentoAnualPorTipo;
import com.ricardo.financeiro.repository.LancamentoRepository;

@Controller
@RequestMapping("/anual")
public class DashboardLancamentosAnualController {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@RequestMapping
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("dashboard/DashboardLancamentoAnual");
		
//		mv.addObject("valorTotalEstoque", livroRepository.valorTotalNoEstoque());
//		mv.addObject("totalItensNoEstoque", livroRepository.totalItensNoEstoque());
//		mv.addObject("totalDeClientes", clienteRepository.totalDeClientes());
//		
//		mv.addObject("vendasNoAno", vendaRepository.valorTotalDuranteAno());
//		mv.addObject("vendasNoMes", vendaRepository.valorTotalDuranteMes());
//		mv.addObject("valorTicketMedioNoAno", lancamentoRepository.totalAnualPorTipo());
		return mv;
	}
	
	 @RequestMapping("/por-tipo")
	    public @ResponseBody List<LancamentoAnualPorTipo> porTipo() {
	        return lancamentoRepository.totalAnualPorTipo();
	    }

}
