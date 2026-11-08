package com.ricardo.financeiro.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.ricardo.financeiro.thymeleaf.processor.ClassForErrorAtributeTagProcessor;
import com.ricardo.financeiro.thymeleaf.processor.MenuLinkAtivoTagProcessor;
import com.ricardo.financeiro.thymeleaf.processor.MessageSuccessOrErrorTagProcessor;
import com.ricardo.financeiro.thymeleaf.processor.OrdenacaoTagProcessor;
import com.ricardo.financeiro.thymeleaf.processor.PaginacaoTagProcessor;

public class SalvavidasDialect extends AbstractProcessorDialect {

	public SalvavidasDialect() {
		super("Ricardo Salvavidas", "salvavidas", StandardDialect.PROCESSOR_PRECEDENCE);
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		
		processadores.add(new ClassForErrorAtributeTagProcessor(dialectPrefix));
		processadores.add(new MessageSuccessOrErrorTagProcessor(dialectPrefix));
		processadores.add(new OrdenacaoTagProcessor(dialectPrefix));
		processadores.add(new PaginacaoTagProcessor(dialectPrefix));
		processadores.add(new MenuLinkAtivoTagProcessor(dialectPrefix));
		
		return processadores;
	}

}
