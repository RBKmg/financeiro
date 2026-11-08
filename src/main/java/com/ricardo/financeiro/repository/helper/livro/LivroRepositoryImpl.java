package com.ricardo.financeiro.repository.helper.livro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ricardo.financeiro.dto.LivroDTO;
import com.ricardo.financeiro.model.Livro;
import com.ricardo.financeiro.repository.filter.LivrosFilter;
import com.ricardo.financeiro.repository.paginacao.PaginacaoUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class LivroRepositoryImpl implements LivroQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PaginacaoUtil paginacaoUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<Livro> filtrar(LivrosFilter filtro, Pageable pageable) {
    	
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Livro> criteria = builder.createQuery(Livro.class);
        Root<Livro> root = criteria.from(Livro.class);

        criteria.select(root);

        adicionarFiltro(filtro, builder, criteria, root);
        adicionarOrdenacao(pageable, builder, criteria, root);

        TypedQuery<Livro> query = entityManager.createQuery(criteria);

        paginacaoUtil.preparar(query, pageable);

        List<Livro> livros = query.getResultList();

        return new PageImpl<>(livros, pageable, total(filtro));
    }
    

    private Long total(LivrosFilter filtro) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<Livro> root = criteria.from(Livro.class);
        criteria.select(builder.count(root));

        adicionarFiltro(filtro, builder, criteria, root);
        return entityManager
                .createQuery(criteria)
                .getSingleResult();
    }

    private void adicionarFiltro(LivrosFilter filtro, CriteriaBuilder builder, CriteriaQuery<?> criteria, Root<Livro> root) {

        if (filtro == null) {
            return;
        }

        List<Predicate> predicates =  new ArrayList<>();

        if (StringUtils.hasText(filtro.getTitulo())) {
            predicates.add(builder.like(builder.lower(root.get("titulo")), "%" + filtro.getTitulo().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getAutor())) {
            predicates.add(builder.like(builder.lower(root.get("autor")), "%" + filtro.getAutor().toLowerCase() + "%"));
        }

        if (filtro.getOrigem() != null) {
            predicates.add(builder.equal(root.get("origem"), filtro.getOrigem()));
        }

        if (filtro.getTipo() != null) {
            predicates.add(builder.equal(root.get("tipo"), filtro.getTipo()));
        }

        if (filtro.getValorDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("valor"), filtro.getValorDe()));
        }

        if (filtro.getValorAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("valor"), filtro.getValorAte()));
        }

        if (!predicates.isEmpty()) {
            criteria.where(builder.and(predicates.toArray(new Predicate[0])));
        }
    }

    private void adicionarOrdenacao(Pageable pageable, CriteriaBuilder builder, CriteriaQuery<Livro> criteria, Root<Livro> root) {

        if (pageable == null || pageable.getSort().isUnsorted()) {
            return;
        }

        List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            if (order.isAscending()) {
                orders.add(builder.asc(root.get(order.getProperty())));
            } else {
                orders.add(builder.desc(root.get(order.getProperty())));
            }
        }
        criteria.orderBy(orders);
    }

    @Override
    public List<LivroDTO> porIsbnOuTitulo(String isbnOuTitulo) {

        String jpql =
                "select new com.ricardo.financeiro.dto.LivroDTO(" +
                "codigo, isbn, titulo, origem, valor, foto) " +
                "from Livro " +
                "where lower(isbn) like lower(:isbnOuTitulo) " +
                "or lower(titulo) like lower(:isbnOuTitulo)";

        return entityManager
                .createQuery(jpql, LivroDTO.class)
                .setParameter("isbnOuTitulo", isbnOuTitulo + "%")
                .getResultList();
    }

    @Override
    public BigDecimal valorTotalNoEstoque() {
        Optional<BigDecimal> optional = Optional.ofNullable(
                    entityManager
                        .createQuery("select sum(estoque * valor) " + "from Livro", BigDecimal.class)
                        .getSingleResult()
                );
        return optional.orElse(BigDecimal.ZERO);
    }

    @Override
    public Long totalItensNoEstoque() {
        Optional<Long> optional = Optional.ofNullable(
                    entityManager
                        .createQuery("select sum(estoque) " + "from Livro", Long.class)
                        .getSingleResult()
                );
        return optional.orElse(0L);
    }
}
