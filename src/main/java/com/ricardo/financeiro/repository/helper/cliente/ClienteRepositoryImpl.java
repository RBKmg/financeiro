package com.ricardo.financeiro.repository.helper.cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ricardo.financeiro.model.Cliente;
import com.ricardo.financeiro.repository.filter.ClientesFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ClienteRepositoryImpl implements ClienteQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> filtrar(ClientesFilter filtro, Pageable pageable) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);

        Root<Cliente> root = query.from(Cliente.class);
        List<Predicate> predicates = adicionarFiltro(filtro, builder, root);

        query.where(predicates.toArray(new Predicate[0]));
        
        paginacaoUtil.adicionarOrdenacao(pageable, builder, query, root);

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(query);

        paginacaoUtil.preparar(typedQuery, pageable);

        List<Cliente> clientes = typedQuery.getResultList();

        return new PageImpl<>(clientes, pageable, total(filtro));
    }

    private Long total(ClientesFilter filtro) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<Cliente> root = query.from(Cliente.class);
        List<Predicate> predicates = adicionarFiltro(filtro, builder, root);

        query.select(builder.count(root));
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager
                .createQuery(query)
                .getSingleResult();
    }

    private List<Predicate> adicionarFiltro(ClientesFilter filtro, CriteriaBuilder builder, Root<Cliente> root) {

        List<Predicate> predicates = new ArrayList<>();

        if (filtro != null) {
            if (filtro.getNome() != null && !filtro.getNome().isBlank()) {
                predicates.add(builder.like(builder.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
            }
            if (filtro.getCpfOuCnpj() != null && !filtro.getCpfOuCnpj().isBlank()) {
                predicates.add(builder.equal(root.get("cpfOuCnpj"), filtro.getCpfOuCnpj()));
            }
        }
        return predicates;
    }

    @Override
    public Long totalDeClientes() {
        Optional<Long> optional = Optional.ofNullable(
        		entityManager
                        .createQuery("select count(*) from Cliente", Long.class)
                        .getSingleResult()
                );
        return optional.orElse(0L);
    }
}