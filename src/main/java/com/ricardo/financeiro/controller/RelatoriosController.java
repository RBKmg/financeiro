package com.ricardo.financeiro.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ricardo.financeiro.dto.PeriodoRelatorio;
import com.ricardo.financeiro.service.RelatoriosService;

@Controller
@RequestMapping("/relatorio")
public class RelatoriosController {
	
	@Autowired
	private RelatoriosService relatoriosService;
	
	@GetMapping("/lancamentos")
	public ModelAndView relatorioLancamentos() {
		ModelAndView mv = new ModelAndView("relatorios/RelatorioLancamentos");
		mv.addObject(new PeriodoRelatorio());
		return mv;
	}
	
	@GetMapping("/vendasEmitidas")
    public ModelAndView relatorioVendasEmitidas() {
		ModelAndView mv = new ModelAndView("relatorios/RelatorioVendasEmitidas");
        mv.addObject(new PeriodoRelatorio());
        return mv;
	}

	
	@PostMapping("/lancamentos")
	public ResponseEntity<byte[]> gerarRelatorioLancamentos(PeriodoRelatorio periodoRelatorio) {
	    Date dataInicio = Date.valueOf(periodoRelatorio.getDataInicio());
	    Date dataFim = Date.valueOf(periodoRelatorio.getDataFim());

	    byte[] pdf = relatoriosService.gerarLancamentos(dataInicio, dataFim);

	    return ResponseEntity.ok()
	    		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio_lancamentos.pdf\"")
	            .contentType(MediaType.APPLICATION_PDF)
	            .contentLength(pdf.length)
	            .body(pdf);
	}
	
	@PostMapping("/vendasEmitidas")
    public ResponseEntity<byte[]> gerarRelatorioVendasEmitidas(PeriodoRelatorio periodoRelatorio) {
    	Date dataInicio = Date.valueOf(periodoRelatorio.getDataInicio());
        Date dataFim = Date.valueOf(periodoRelatorio.getDataFim());

        byte[] pdf = relatoriosService.gerarVendasEmitidas(dataInicio, dataFim);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio_vendas_emitidas.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
