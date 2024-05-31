package com.appcenter.practice.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBucket is a Querydsl query type for Bucket
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBucket extends EntityPathBase<Bucket> {

    private static final long serialVersionUID = 1979853062L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBucket bucket = new QBucket("bucket");

    public final com.appcenter.practice.common.QBaseEntity _super = new com.appcenter.practice.common.QBaseEntity(this);

    public final BooleanPath completed = createBoolean("completed");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DatePath<java.time.LocalDate> deadLine = createDate("deadLine", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<Todo, QTodo> todoList = this.<Todo, QTodo>createList("todoList", Todo.class, QTodo.class, PathInits.DIRECT2);

    public QBucket(String variable) {
        this(Bucket.class, forVariable(variable), INITS);
    }

    public QBucket(Path<? extends Bucket> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBucket(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBucket(PathMetadata metadata, PathInits inits) {
        this(Bucket.class, metadata, inits);
    }

    public QBucket(Class<? extends Bucket> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

