package com.ricardo.financeiro.repository.helper.venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.dto.GraficoVendasPorMes;
import com.ricardo.financeiro.dto.VendaOrigem;
import com.ricardo.financeiro.model.StatusVenda;
import com.ricardo.financeiro.model.TipoPessoa;
import com.ricardo.financeiro.model.Venda;
import com.ricardo.financeiro.repository.filter.VendasFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class VendaRepositoryImpl implements VendaQueries {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Transactional(readOnly = true)
	@Override
	public Page<Venda> filtrar(VendasFilter filtro, Pageable pageable) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Venda> criteriaQuery = criteriaBuilder.createQuery(Venda.class);
		
		Root<Venda> root = criteriaQuery.from(Venda.class);
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, root);
		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		paginacaoUtil.adicionarOrdenacao(pageable, criteriaBuilder, criteriaQuery, root);
		
		TypedQuery<Venda> typedQuery = entityManager.createQuery(criteriaQuery);
		
		paginacaoUtil.preparar(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, total(filtro));
	}
	
	private Long total(VendasFilter filtro) {		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		Root<Venda> root = criteriaQuery.from(Venda.class);		
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, root);
		
		criteriaQuery.select(criteriaBuilder.count(root));		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		return entityManager
				.createQuery(criteriaQuery)
				.getSingleResult();
	}
	
	private List<Predicate> adicionarFiltro(VendasFilter filtro, CriteriaBuilder criteriaBuilder, Root<Venda> root) {		
		List<Predicate> predicates = new ArrayList<>();
		
		Join<Venda, ?> cliente = root.join("cliente");
		
		if (filtro != null) {			
			if (filtro.getCodigo() != null) {
				predicates.add(criteriaBuilder.equal(root.get("codigo"), filtro.getCodigo()));
			}			
			if (filtro.getStatus() != null) {
				predicates.add(criteriaBuilder.equal(root.get("status"), filtro.getStatus()));
			}			
			if (filtro.getDataCriacaoDe() != null) {
				LocalDateTime desde = LocalDateTime.of(filtro.getDataCriacaoDe(), LocalTime.of(0, 0));				
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataCriacao"), desde));
			}			
			if (filtro.getDataCriacaoAte() != null) {
				LocalDateTime ate =	LocalDateTime.of(filtro.getDataCriacaoAte(), LocalTime.of(23, 59));				
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataCriacao"), ate));
			}			
			if (filtro.getValorMinimo() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("valorTotal"), filtro.getValorMinimo()));
			}			
			if (filtro.getValorMaximo() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("valorTotal"), filtro.getValorMaximo()));
			}			
			if (filtro.getNomeCliente() != null && !filtro.getNomeCliente().isBlank()) {				
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(cliente.get("nome")), "%" + filtro.getNomeCliente().toLowerCase() + "%"));
			}			
			if (filtro.getCpfOuCnpj() != null && !filtro.getCpfOuCnpj().isBlank()) {				
				predicates.add(criteriaBuilder.equal(cliente.get("cpfOuCnpj"), TipoPessoa.removerFormatacao(filtro.getCpfOuCnpj())));
			}
		}		
		return predicates;
	}
	
	@Transactional(readOnly = true)
	@Override
	public Venda buscarComItens(Long codigo) {		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Venda> criteriaQuery = criteriaBuilder.createQuery(Venda.class);
		
		Root<Venda> root = criteriaQuery.from(Venda.class);		
		root.fetch("itens", jakarta.persistence.criteria.JoinType.LEFT);
		
		criteriaQuery.select(root);		
		criteriaQuery.where(criteriaBuilder.equal(root.get("codigo"), codigo));		
		criteriaQuery.distinct(true);
		
		return entityManager
				.createQuery(criteriaQuery)
				.getResultStream()
				.findFirst()
				.orElse(null);
	}
	
	@Override
	public BigDecimal valorTotalDuranteAno() {
		Optional<BigDecimal> optional = Optional.ofNullable(entityManager
				.createQuery("select sum(valorTotal) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
			.setParameter("ano", Year.now().getValue())
			.setParameter("status", StatusVenda.EMITIDA)
			.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal valorTotalDuranteMes() {
		Optional<BigDecimal> optional = Optional.ofNullable(entityManager
				.createQuery("select sum(valorTotal) from Venda where month(dataCriacao) = :mes and status = :status", BigDecimal.class)
			.setParameter("mes", YearMonth.now().getMonthValue())
			.setParameter("status", StatusVenda.EMITIDA)
			.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@Override
	public BigDecimal valorTotalTicketMedio() {
		Optional<BigDecimal> optional = Optional.ofNullable(entityManager
				.createQuery("select sum(valorTotal)/count(*) from Venda where year(dataCriacao) = :ano and status = :status", BigDecimal.class)
			.setParameter("ano", Year.now().getValue())
			.setParameter("status", StatusVenda.EMITIDA)
			.getSingleResult());
		return optional.orElse(BigDecimal.ZERO);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GraficoVendasPorMes> totalVendasPorMes() {
		List<GraficoVendasPorMes> vendasMes = entityManager.createNamedQuery("Vendas.totalPorMes")
				.getResultList();
		
		LocalDate hoje = LocalDate.now();
		
		for(int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", hoje.getYear(), hoje.getMonthValue());
			
			boolean possuiMes =
					vendasMes.stream()
					.filter(v -> v.getMes().equals(mesIdeal))
					.findAny()
					.isPresent();
			
			if(!possuiMes) {
				vendasMes.add(i - 1, new GraficoVendasPorMes(mesIdeal, 0));
			}			
			hoje = hoje.minusMonths(1);
		}		
		return vendasMes;
	}
	
	@Override
	public List<VendaOrigem> totalPorOrigem() {
		List<VendaOrigem> vendasNacionalidade =	entityManager.createNamedQuery("Vendas.porOrigem",	VendaOrigem.class)
				.getResultList();
		
		LocalDate now = LocalDate.now();
		
		for (int i = 1; i <= 6; i++) {
			String mesIdeal = String.format("%d/%02d", now.getYear(), now.getMonth().getValue());
			
			boolean possuiMes = vendasNacionalidade.stream()
					.filter(v -> v.getMes().equals(mesIdeal))
					.findAny()
					.isPresent();
			
			if (!possuiMes) {
				vendasNacionalidade.add(i - 1, new VendaOrigem(mesIdeal, 0, 0));
			}			
			now = now.minusMonths(1);
		}		
		return vendasNacionalidade;
	}

}
