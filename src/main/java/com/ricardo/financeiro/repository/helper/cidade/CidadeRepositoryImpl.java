package com.ricardo.financeiro.repository.helper.cidade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Cidade;
import com.ricardo.financeiro.repository.filter.CidadesFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CidadeRepositoryImpl implements CidadeQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<Cidade> filtrar(CidadesFilter filter, Pageable pageable) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Cidade> query = builder.createQuery(Cidade.class);

        Root<Cidade> root = query.from(Cidade.class);
        List<Predicate> predicates = adicionarFiltro(filter, builder, root);

        query.where(predicates.toArray(new Predicate[0]));
        
        paginacaoUtil.adicionarOrdenacao(pageable, builder, query, root);
        
        TypedQuery<Cidade> typedQuery = manager.createQuery(query);

        paginacaoUtil.preparar(typedQuery, pageable);
        List<Cidade> cidades = typedQuery.getResultList();

        return new PageImpl<>(cidades, pageable, total(filter));
    }

    private Long total(CidadesFilter filter) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Cidade> root = query.from(Cidade.class);
        List<Predicate> predicates = adicionarFiltro(filter, builder, root);

        query.select(builder.count(root));
        query.where(predicates.toArray(new Predicate[0]));

        return manager
                .createQuery(query)
                .getSingleResult();
    }

    private List<Predicate> adicionarFiltro(CidadesFilter filter, CriteriaBuilder builder, Root<Cidade> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter != null) {
            if (filter.getEstado() != null) {
                predicates.add(builder.equal(root.get("estado"), filter.getEstado()));
            }
            if (filter.getNome() != null && !filter.getNome().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
            }
        }
        return predicates;
    }
}