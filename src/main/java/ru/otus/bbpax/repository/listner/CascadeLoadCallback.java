package ru.otus.bbpax.repository.listner;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Slf4j
public class CascadeLoadCallback implements ReflectionUtils.FieldCallback {
    private final MongoOperations mongoOps;
    private final Document source;

    public CascadeLoadCallback(Document source, MongoOperations mongoOps) {
        this.mongoOps = mongoOps;
        this.source = source;
    }

    @Override
    public void doWith(@NonNull Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);
        log.info("DO nothing with: field[{}] = {}", field.getName(), source.get(field.getName()));
    }
}
