package ru.otus.bbpax.repository.listner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.otus.bbpax.entity.Book;

import java.lang.reflect.Field;

@Slf4j
public class CascadeLoadMongoEventListener extends AbstractMongoEventListener<Book> {
    private final MongoOperations mongoOps;

    public CascadeLoadMongoEventListener(MongoOperations mongoOps) {
        this.mongoOps = mongoOps;
    }

    @Override
    public void onAfterLoad(@NonNull AfterLoadEvent<Book> event) {
        super.onAfterLoad(event);
        log.info("after load I have: {} of type - {} \n document:\n{}",
                event.getSource(),
                event.getType(),
                event.getDocument());

        ReflectionUtils.doWithFields(
                event.getType(),
                new CascadeLoadCallback(event.getSource(), mongoOps),
                this::hasCascadeLoadAnnotation);
    }

    private boolean hasCascadeLoadAnnotation(Field field) {
        log.debug("Field[{}] has Dbref: {} and Cascade: {}",
                field.getName(), field.isAnnotationPresent(DBRef.class),
                field.isAnnotationPresent(CascadeLoad.class));
        return field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeLoad.class);
    }
}
