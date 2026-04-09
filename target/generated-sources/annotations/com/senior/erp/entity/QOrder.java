package com.senior.erp.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 677364383L;

    public static final QOrder order = new QOrder("order1");

    public final NumberPath<java.math.BigDecimal> discount = createNumber("discount", java.math.BigDecimal.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final ListPath<OrderItem, QOrderItem> items = this.<OrderItem, QOrderItem>createList("items", OrderItem.class, QOrderItem.class, PathInits.DIRECT2);

    public final EnumPath<OrderStatus> status = createEnum("status", OrderStatus.class);

    public final NumberPath<java.math.BigDecimal> total = createNumber("total", java.math.BigDecimal.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

