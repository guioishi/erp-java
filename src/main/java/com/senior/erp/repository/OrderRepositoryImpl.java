package com.senior.erp.repository;

import com.senior.erp.dto.OrderFilter;
import com.senior.erp.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Order> findAllWithFilter(OrderFilter filter, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getMinTotal() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("total"), filter.getMinTotal()));
        }

        if (filter.getMaxTotal() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("total"), filter.getMaxTotal()));
        }

        query.where(predicates.toArray(new Predicate[0]));

        if (pageable.getSort().isSorted()) {
            List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();

            for (Sort.Order sort : pageable.getSort()) {
                switch (sort.getProperty()) {
                    case "status":
                        orders.add(sort.isAscending()
                                ? cb.asc(root.get("status"))
                                : cb.desc(root.get("status")));
                        break;

                    case "total":
                        orders.add(sort.isAscending()
                                ? cb.asc(root.get("total"))
                                : cb.desc(root.get("total")));
                        break;

                    default:
                        break;
                }
            }

            if (!orders.isEmpty()) {
                query.orderBy(orders);
            }
        }

        TypedQuery<Order> typedQuery = em.createQuery(query);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Order> resultList = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Order> countRoot = countQuery.from(Order.class);

        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.getStatus() != null) {
            countPredicates.add(cb.equal(countRoot.get("status"), filter.getStatus()));
        }

        if (filter.getMinTotal() != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("total"), filter.getMinTotal()));
        }

        if (filter.getMaxTotal() != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("total"), filter.getMaxTotal()));
        }

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}