package com.ricardo.financeiro.repository.paginacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Component
public class PaginacaoUtil {

    public void preparar(TypedQuery<?> query, Pageable pageable) {

        int primeiroRegistro = pageable.getPageNumber() * pageable.getPageSize();

        query.setFirstResult(primeiroRegistro);
        query.setMaxResults(pageable.getPageSize());
    }
    
    
    public <T> void adicionarOrdenacao(Pageable pageable, CriteriaBuilder builder, CriteriaQuery<T> criteria, Root<T> root) {
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
}
