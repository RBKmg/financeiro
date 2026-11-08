package com.ricardo.financeiro.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ricardo.financeiro.repository.LancamentoRepository;

@Controller
public class DashboardLancamentosController {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@RequestMapping("/")
	public ModelAndView dashboard() {
		ModelAndView mv = new ModelAndView("dashboard/DashboardLancamento"); 
		
		BigDecimal valorReceita = lancamentoRepository.totalDeReceitaNoMes();
		BigDecimal valorDespesa = lancamentoRepository.totalDeDespesaNoMes();
		BigDecimal saldoDoMes = valorReceita.subtract(valorDespesa);
		
		mv.addObject("totalReceita", valorReceita);
		mv.addObject("totalDespesa", valorDespesa);
		mv.addObject("saldoDoMes", saldoDoMes);
		return mv;
	}	

}
