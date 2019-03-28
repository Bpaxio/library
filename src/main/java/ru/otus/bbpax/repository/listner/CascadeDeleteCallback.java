package ru.otus.bbpax.repository.listner;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.otus.bbpax.repository.listner.callback.CallbackEntity;
import ru.otus.bbpax.repository.listner.callback.CallbackEntityBuilder;
import ru.otus.bbpax.repository.listner.callback.CallbackEntityBuilderImpl;

import java.lang.reflect.Field;

@Slf4j
public class CascadeDeleteCallback implements ReflectionUtils.FieldCallback {
    private final MongoOperations mongoOps;
    private final CallbackEntityBuilder builder;

    public CascadeDeleteCallback(@NonNull String collection,
                                 @NonNull Document source,
                                 @NonNull MongoOperations mongoOps) {
        this.mongoOps = mongoOps;

        this.builder = new CallbackEntityBuilderImpl()
                .forSourceCollection(collection)
                .forSource(source);
    }

    @Override
    public void doWith(@NonNull Field field) throws IllegalArgumentException {
        ReflectionUtils.makeAccessible(field);

        CallbackEntity callbackEntity = builder
                .forField(field)
                .buildCallbackEntity();

        log.info("Remove for: {}", callbackEntity);

        mongoOps.findAllAndRemove(
                callbackEntity.getQuery(),
                callbackEntity.getTargetClass(),
                callbackEntity.getTargetFieldCollectionName()
        );
        return;
    }

}
