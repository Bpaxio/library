package ru.otus.bbpax.repository.listner;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.otus.bbpax.repository.listner.callback.CallbackEntityBuilder;
import ru.otus.bbpax.repository.listner.callback.CallbackEntityBuilderImpl;

import java.lang.reflect.Field;

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
        log.info("do nothing for {} of {}", field.getName(), source);
    }


}
