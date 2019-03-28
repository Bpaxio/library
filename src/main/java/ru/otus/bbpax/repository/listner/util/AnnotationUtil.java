package ru.otus.bbpax.repository.listner.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.DBRef;
import ru.otus.bbpax.repository.listner.annotation.Cascade;
import ru.otus.bbpax.repository.listner.annotation.CascadeType;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class AnnotationUtil {

    public static boolean hasCascadeDeleteFeature(Field field) {
        return hasFeature(field, CascadeType.DELETE);
    }

    public static boolean hasCascadeCreateFeature(Field field) {
        return hasFeature(field, CascadeType.INSERT);
    }

    public static boolean hasFeature(Field field, CascadeType featureType) {
        Cascade annotation = getCascadeAnnotationOf(field);
        return Objects.nonNull(annotation)
                && (annotation.type().equals(featureType)
                || annotation.type().equals(CascadeType.ALL));
    }

    public static Cascade getCascadeAnnotationOf(Field field) {
        return hasCascadeAnnotation(field)
                ? field.getAnnotation(Cascade.class)
                : null;
    }

    public static boolean hasCascadeAnnotation(Field field) {
        log.debug("Field[{}] has Dbref: {} and Cascade: {}",
                field.getName(), field.isAnnotationPresent(DBRef.class),
                field.isAnnotationPresent(Cascade.class));
        return field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(Cascade.class);
    }

    public static String getTargetCollectionNameFor(Field field) {
        return field.getAnnotation(Cascade.class).collection();
    }
}
