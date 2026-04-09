package com.senior.erp.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

import com.senior.erp.dto.OrderFilter;
import com.senior.erp.entity.Order;
import com.senior.erp.entity.QOrder;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Order> findAllWithFilter(OrderFilter filter, Pageable pageable) {

        QOrder order = QOrder.order;

        JPAQuery<Order> query = queryFactory.selectFrom(order);

        BooleanBuilder predicate = new BooleanBuilder();

        if (filter != null) {

            if (filter.getStatus() != null) {
                predicate.and(order.status.eq(filter.getStatus()));
            }

            if (filter.getMinTotal() != null) {
                predicate.and(order.total.goe(filter.getMinTotal()));
            }

            if (filter.getMaxTotal() != null) {
                predicate.and(order.total.loe(filter.getMaxTotal()));
            }
        }

        query.where(predicate);

        if (pageable.getSort().isSorted()) {

            PathBuilder<Order> entityPath = new PathBuilder<>(Order.class, "order");

            pageable.getSort().forEach(sort -> {

                com.querydsl.core.types.Order direction =
                    sort.isAscending()
                        ? com.querydsl.core.types.Order.ASC
                        : com.querydsl.core.types.Order.DESC;

                ComparableExpressionBase<?> path =
                        entityPath.getComparable(sort.getProperty(), Comparable.class);

                query.orderBy(new OrderSpecifier<>(direction, path));
            });
        }

        long total = queryFactory
                .selectFrom(order)
                .where(predicate)
                .fetch()
                .size();

        List<Order> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}