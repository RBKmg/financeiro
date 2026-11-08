package com.ricardo.financeiro.repository.helper.categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Categoria;
import com.ricardo.financeiro.repository.filter.CategoriasFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CategoriaRepositoryImpl implements CategoriaQueries {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;

	
	@Override
	@Transactional(readOnly = true)
	public Page<Categoria> filtrar(CategoriasFilter filtro, Pageable pageable) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Categoria> query = builder.createQuery(Categoria.class);

        Root<Categoria> root = query.from(Categoria.class);
        List<Predicate> predicates = adicionarFiltro(filtro, builder, root);

        query.where(predicates.toArray(new Predicate[0]));
        
        paginacaoUtil.adicionarOrdenacao(pageable, builder, query, root);
                
        TypedQuery<Categoria> typedQuery = entityManager.createQuery(query);

        paginacaoUtil.preparar(typedQuery, pageable);

        List<Categoria> categorias = typedQuery.getResultList();

        return new PageImpl<>(categorias, pageable, total(filtro));
	}
	
	
	private Long total(CategoriasFilter filtro) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Categoria> root = query.from(Categoria.class);

        List<Predicate> predicates = adicionarFiltro(filtro, builder, root);
        query.select(builder.count(root));
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager
                .createQuery(query)
                .getSingleResult();
	}

	private List<Predicate> adicionarFiltro(CategoriasFilter filtro, CriteriaBuilder builder, Root<Categoria> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (filtro != null && filtro.getNome() != null && !filtro.getNome().isBlank()) {
            predicates.add(builder.like(builder.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase(Locale.ROOT) + "%"));
        }
        return predicates;
    }

}
