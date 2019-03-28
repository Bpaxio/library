package ru.otus.bbpax.repository.listner.notnamed;

import com.mongodb.DBRef;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.otus.bbpax.repository.listner.util.AnnotationUtil.getTargetCollectionNameFor;
import static ru.otus.bbpax.repository.listner.util.SomeUtil.getIdFieldValue;
import static ru.otus.bbpax.repository.listner.util.SomeUtil.getTargetClass;
import static ru.otus.bbpax.repository.listner.util.SomeUtil.notMongoEntity;

@Slf4j
public class CallbackEntityBuilderImpl implements CallbackEntityBuilder {
    private Field field;
    private Document source;
    private String sourceCollection;

    @Override
    public CallbackEntity buildCallbackEntity() {
        assert Objects.nonNull(field)
                && Objects.nonNull(source)
                && Objects.nonNull(sourceCollection);

        Class<?> targetClass = getTargetClass(field);
        if(notMongoEntity(targetClass)) {
            log.warn("Wrong mapping: field '{}' of '{}' refers to the class without 'Document' annotation",
                    field.getName(), field.getDeclaringClass());
            return null;
        }
        String targetFieldCollectionName = getTargetCollectionNameFor(field);

        Query query = getQueryForTarget(source.get(field.getName()));

        return new CallbackEntity(targetClass, query, targetFieldCollectionName);
    }

    private Query getQueryForTarget(Object target) {
        log.info("building query for target: {}", target);
        if (target != null) {
            return getQuery(target);
        }
        return getQueryForNull();
    }

    private Query getQuery(@NonNull Object target) {
        Criteria criteria = Criteria.where("_id");
        Query query;
        if (target instanceof Iterable) {
            List<ObjectId> ids = new ArrayList<>();
            for (Object element : ((Iterable) target)) {
                ids.add(new ObjectId(getIdFieldValue(element)));
            }
            query = new Query(criteria.in(ids));
        } else {
            String targetId = getIdFieldValue(target);
            query = new Query(
                    criteria.in(new ObjectId(targetId)));
        }
        return query;
    }

    private Query getQueryForNull() {
        log.info("Ref is null. Try to find it using ref to collection: {} for {_class: {}, _id: {}}",
                sourceCollection, source.getString("_class"), source.get("_id"));

        return new Query(Criteria.where(source.getString("_class"))
                .is(new DBRef(sourceCollection, new ObjectId(source.get("_id").toString()))));
    }

    @Override
    public CallbackEntityBuilderImpl forField(Field field) {
        this.field = field;
        return this;
    }

    @Override
    public CallbackEntityBuilderImpl forSource(Document source) {
        this.source = source;
        return this;
    }

    @Override
    public CallbackEntityBuilderImpl forSourceCollection(String sourceCollection) {
        this.sourceCollection = sourceCollection;
        return this;
    }
}
