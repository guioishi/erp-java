package com.senior.erp.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

import com.senior.erp.dto.ProductFilter;
import com.senior.erp.entity.Product;
import com.senior.erp.entity.QProduct;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Product> findAllWithFilter(ProductFilter filter, Pageable pageable) {

        QProduct product = QProduct.product;

        JPAQuery<Product> query = queryFactory.selectFrom(product);

        if (filter != null) {

            if (filter.getName() != null && !filter.getName().isBlank()) {
                query.where(product.name.containsIgnoreCase(filter.getName()));
            }

            if (filter.getType() != null) {
                query.where(product.type.eq(filter.getType()));
            }

            if (filter.getActive() != null) {
                query.where(product.active.eq(filter.getActive()));
            }
        }

        List<String> allowedSorts = List.of("name", "type", "active", "price");

        if (pageable.getSort().isSorted()) {

            PathBuilder<Product> entityPath = new PathBuilder<>(Product.class, "product");

            pageable.getSort().forEach(sort -> {

                if (!allowedSorts.contains(sort.getProperty())) {
                    return;
                }

                com.querydsl.core.types.Order direction =
                        sort.isAscending()
                                ? com.querydsl.core.types.Order.ASC
                                : com.querydsl.core.types.Order.DESC;

                ComparableExpressionBase<?> path =
                        entityPath.getComparable(sort.getProperty(), Comparable.class);

                query.orderBy(new OrderSpecifier<>(direction, path));
            });
        }

        long total = queryFactory.selectFrom(product).fetch().size();

        List<Product> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}