package com.ricardo.financeiro.repository.helper.lancamento;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.dto.LancamentoAnualPorTipo;
import com.ricardo.financeiro.dto.LancamentoEstatisticaCategoria;
import com.ricardo.financeiro.dto.LancamentoEstatisticaDia;
import com.ricardo.financeiro.model.Lancamento;
import com.ricardo.financeiro.model.Lancamento_;
import com.ricardo.financeiro.model.TipoLancamento;
import com.ricardo.financeiro.repository.filter.LancamentosFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class LancamentoRepositoryImpl implements LancamentoQueries {


	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	@Transactional(readOnly = true)
	public Page<Lancamento> filtrar(LancamentosFilter filtro, Pageable pageable) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();	
		CriteriaQuery<Lancamento> criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);	
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, root);
		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		paginacaoUtil.adicionarOrdenacao(pageable, criteriaBuilder, criteriaQuery, root);
		
		TypedQuery<Lancamento> typedQuery = entityManager.createQuery(criteriaQuery);
		
		paginacaoUtil.preparar(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filtro));
	}
	
	private Long total(LancamentosFilter filtro) {		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);		
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, root);
		
		criteriaQuery.select(criteriaBuilder.count(root));		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		return entityManager
				.createQuery(criteriaQuery)
				.getSingleResult();
	}
	
	private List<Predicate> adicionarFiltro(LancamentosFilter filtro, CriteriaBuilder criteriaBuilder, Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>();		
		if (filtro != null) {			
			if (filtro.getDescricao() != null && !filtro.getDescricao().isBlank()) {				
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get("descricao")), "%" + filtro.getDescricao().toLowerCase() + "%"));
			}
			
			if (filtro.getDataVencimentoDe() != null) {				
				predicates.add(
						criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), filtro.getDataVencimentoDe()));
			}
			
			if (filtro.getDataVencimentoAte() != null) {				
				predicates.add(
						criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), filtro.getDataVencimentoDe()));
			}
		}		
		return predicates;
	}
	
	// ESTATISTICA POR CATEGORIA Dashboard - grafico de pizza -------------------------------------------------
	@Override
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder
				.createQuery(LancamentoEstatisticaCategoria.class);
		
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class,
				root.get(Lancamento_.categoria),
				criteriaBuilder.sum(root.get(Lancamento_.valor)) ));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
		
		criteriaQuery.groupBy(root.get(Lancamento_.categoria));		
		TypedQuery<LancamentoEstatisticaCategoria> typedQuery = entityManager.createQuery(criteriaQuery);		
		
		return typedQuery.getResultList();
	}
	
	// ESTATISTICA POR DIA Dashboard - grafico de linha -------------------------------------------------------------
	@Override
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			
		CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = criteriaBuilder
			.createQuery(LancamentoEstatisticaDia.class);
			
		Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
			
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDia.class,
			root.get(Lancamento_.tipo),
			root.get(Lancamento_.dataVencimento),
			criteriaBuilder.sum(root.get(Lancamento_.valor)) ));
			
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
			
		criteriaQuery.where(
			criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
			criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia));
			
		criteriaQuery.groupBy(
			root.get(Lancamento_.tipo), 
			root.get(Lancamento_.dataVencimento));
			
		TypedQuery<LancamentoEstatisticaDia> typedQuery = entityManager.createQuery(criteriaQuery);		
			
		return typedQuery.getResultList();
	}
	
	// BOX ----------------------------------------------------------------------------------------------------------
	@Override
	public BigDecimal totalDeReceitaNoMes() {
		Optional<BigDecimal> optional = Optional.ofNullable(entityManager
				.createQuery("select sum(valor) from Lancamento where month(dataVencimento) = :mes and tipo = :tipo", BigDecimal.class)
			.setParameter("mes", YearMonth.now().getMonthValue())
			.setParameter("tipo", TipoLancamento.RECEITA)
			.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal totalDeDespesaNoMes() {
		Optional<BigDecimal> optional = Optional.ofNullable(entityManager
				.createQuery("select sum(valor) from Lancamento where month(dataVencimento) = :mes and tipo = :tipo", BigDecimal.class)
			.setParameter("mes", YearMonth.now().getMonthValue())
			.setParameter("tipo", TipoLancamento.DESPESA)
			.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}

	// ESTATISTICA ANUAL Dashboard - grafico de BARRAS -------------------------------------------------------------
	@Override
	public List<LancamentoAnualPorTipo> totalAnualPorTipo() {
		List<LancamentoAnualPorTipo> lancamentosPorTipo = entityManager.createNamedQuery("LancamentosAnual.porTipo", LancamentoAnualPorTipo.class)
				.getResultList();
		
		LocalDate now = LocalDate.now();
		
		for (int i = 1; i <= 12; i++) {
			String mesIdeal = String.format("%d/%02d", now.getYear(), now.getMonth().getValue());
			
			boolean possuiMes = lancamentosPorTipo.stream()
					.filter(v -> v.getMes().equals(mesIdeal))
					.findAny()
					.isPresent();
			
			if (!possuiMes) {
				lancamentosPorTipo.add(i - 1, new LancamentoAnualPorTipo(mesIdeal, BigDecimal.ZERO, BigDecimal.ZERO));
			}			
			now = now.minusMonths(1);
		}		
		return lancamentosPorTipo;
	}

}
