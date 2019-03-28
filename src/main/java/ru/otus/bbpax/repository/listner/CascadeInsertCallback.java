package ru.otus.bbpax.repository.listner;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.otus.bbpax.repository.listner.notnamed.CallbackEntity;
import ru.otus.bbpax.repository.listner.notnamed.CallbackEntityBuilder;
import ru.otus.bbpax.repository.listner.notnamed.CallbackEntityBuilderImpl;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class CascadeInsertCallback implements ReflectionUtils.FieldCallback {
    private final MongoOperations mongoOps;
    private final CallbackEntityBuilder builder;
    private final Document source;

    public CascadeInsertCallback(@NonNull String collection,
                                 @NonNull Document source,
                                 @NonNull MongoOperations mongoOps) {
        this.source = source;
        this.mongoOps = mongoOps;
        builder = new CallbackEntityBuilderImpl()
                .forSource(source)
                .forSourceCollection(collection);
    }

    @Override
    public void doWith(@NonNull Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        CallbackEntity callbackEntity = builder
                .forField(field)
                .buildCallbackEntity();
        List<?> found = mongoOps.find(callbackEntity.getQuery(),
                callbackEntity.getTargetClass(),
                callbackEntity.getTargetFieldCollectionName());
        if (found.isEmpty()) {
            log.info("try to insert");
            mongoOps.upsert(callbackEntity.getQuery(),
                    Update.fromDocument(source.get(field.getName(), Document.class)),
                    callbackEntity.getTargetClass(),
                    callbackEntity.getTargetFieldCollectionName()
            );
        }

        log.info("Insert for: {}", callbackEntity);
    }


}
