package ru.otus.bbpax.repository.listner.notnamed;

import org.springframework.data.mongodb.core.query.Query;

public class CallbackEntity {
    private final Class<?> targetClass;
    private final Query query;
    private final String targetFieldCollectionName;

    public CallbackEntity(Class<?> targetClass, Query query, String targetFieldCollectionName) {
        this.targetClass = targetClass;
        this.query = query;
        this.targetFieldCollectionName = targetFieldCollectionName;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Query getQuery() {
        return query;
    }

    public String getTargetFieldCollectionName() {
        return targetFieldCollectionName;
    }

    @Override
    public String toString() {
        return "CallbackEntity{" +
                "targetClass=" + targetClass +
                ", query=" + query +
                ", targetFieldCollectionName='" + targetFieldCollectionName + '\'' +
                '}';
    }
}
