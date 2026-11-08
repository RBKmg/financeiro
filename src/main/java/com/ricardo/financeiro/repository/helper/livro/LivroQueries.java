package com.ricardo.financeiro.repository.helper.livro;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ricardo.financeiro.dto.LivroDTO;
import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.repository.filter.LivrosFilter;

public interface LivroQueries {
	
	public Page<Livro> filtrar(LivrosFilter filtro, Pageable pageable);
	
	public List<LivroDTO> porIsbnOuTitulo(String isbnOuNome);
	
	public BigDecimal valorTotalNoEstoque();
	public Long totalItensNoEstoque();

}
