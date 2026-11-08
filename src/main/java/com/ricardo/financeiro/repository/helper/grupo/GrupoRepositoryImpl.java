package com.ricardo.financeiro.repository.helper.grupo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Grupo;
import com.ricardo.financeiro.model.GrupoPermissao;
import com.ricardo.financeiro.model.Permissao;
import com.ricardo.financeiro.repository.filter.GruposFilter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class GrupoRepositoryImpl implements GrupoQueries {


	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	@Override
	public List<Grupo> filtrar(GruposFilter filtro) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Grupo> criteriaQuery = criteriaBuilder.createQuery(Grupo.class);
		
		Root<Grupo> root = criteriaQuery.from(Grupo.class);
		
		List<Predicate> predicates = adicionarFiltro(filtro, criteriaBuilder, criteriaQuery, root);		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		
		criteriaQuery.distinct(true);
		
		TypedQuery<Grupo> typedQuery =	entityManager.createQuery(criteriaQuery);		
		return typedQuery.getResultList();
	}
	
	private List<Predicate> adicionarFiltro(GruposFilter filtro, CriteriaBuilder criteriaBuilder, CriteriaQuery<Grupo> criteriaQuery, Root<Grupo> root) {
		
		List<Predicate> predicates = new ArrayList<>();		
		if (filtro != null) {			
			if (filtro.getNome() != null && !filtro.getNome().isBlank()) {				
				predicates.add(
						criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
			}			
			if (filtro.getPermissoes() != null && !filtro.getPermissoes().isEmpty()) {				
				for (Permissao permissao : filtro.getPermissoes()) {
					
					Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);					
					Root<GrupoPermissao> grupoPermissao = subquery.from(GrupoPermissao.class);					
					subquery.select(criteriaBuilder.literal(1));
					
					subquery.where(
							criteriaBuilder.equal(grupoPermissao.get("id").get("grupo").get("codigo"),root.get("codigo")),							
							criteriaBuilder.equal(grupoPermissao.get("id").get("permissao").get("codigo"),
									permissao.getCodigo())
					);					
					predicates.add(criteriaBuilder.exists(subquery));
				}
			}
		}		
		return predicates;
	}
	
	@Transactional(readOnly = true)
	@Override
	public Grupo buscarGrupoComPermissoes(Long codigo) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();		
		CriteriaQuery<Grupo> criteriaQuery = criteriaBuilder.createQuery(Grupo.class);
		
		Root<Grupo> root = criteriaQuery.from(Grupo.class);		
		root.fetch("permissoes", jakarta.persistence.criteria.JoinType.LEFT);
		
		criteriaQuery.select(root);		
		criteriaQuery.where(criteriaBuilder.equal(root.get("codigo"), codigo));		
		criteriaQuery.distinct(true);
		
		return entityManager
				.createQuery(criteriaQuery)
				.getResultStream()
				.findFirst()
				.orElse(null);
	}

}
