package ru.otus.bbpax.repository.listner;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.otus.bbpax.repository.listner.util.AnnotationUtil;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CascadeActionsMongoEventListener extends AbstractMongoEventListener<Object> {
    private final MongoOperations mongoOps;

    public CascadeActionsMongoEventListener(MongoOperations mongoOps) {
        this.mongoOps = mongoOps;
    }

    @Override
    public void onBeforeSave(@NonNull BeforeSaveEvent<Object> event) {
        super.onBeforeSave(event);
        log.debug("Before save source: {} of class: {} \n document:\n{}",
                event.getSource(),
                event.getSource().getClass(),
                event.getDocument());

        assert Objects.nonNull(event.getCollectionName())
                && Objects.nonNull(event.getDocument());

        ReflectionUtils.doWithFields(
                event.getSource().getClass(),
                new CascadeInsertCallback(event.getCollectionName(), event.getDocument(), mongoOps),
                AnnotationUtil::hasCascadeCreateFeature);
    }

    @Override
    public void onBeforeDelete(@NonNull BeforeDeleteEvent<Object> event) {
        super.onBeforeDelete(event);
        log.info("Before delete source: {} of type: {} and class: {} \n document:\n{}",
                event.getSource(),
                event.getType(),
                event.getSource().getClass(),
                event.getDocument());

        assert Objects.nonNull(event.getCollectionName())
                && Objects.nonNull(event.getDocument())
                && Objects.nonNull(event.getType());

        List<Document> source = mongoOps.find(new Query(Criteria.where("_id").is(event.getDocument().get("_id"))), Document.class, event.getCollectionName());
        log.info("loaded source: {}", source);
        source.forEach(docSource ->
            ReflectionUtils.doWithFields(
                    event.getType(),
                    new CascadeDeleteCallback(event.getCollectionName(), docSource, mongoOps),
                    AnnotationUtil::hasCascadeDeleteFeature)
        );
    }
}
