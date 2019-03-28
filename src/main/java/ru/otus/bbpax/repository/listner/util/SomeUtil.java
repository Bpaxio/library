package ru.otus.bbpax.repository.listner.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

@Slf4j
public class SomeUtil {

    public static boolean notMongoEntity(Class<?> targetClass) {
        Document annotation = targetClass.getAnnotation(Document.class);
        return Objects.isNull(annotation);
    }

    public static Class<?> getTargetClass(Field field) {
        if (Iterable.class.isAssignableFrom(field.getType())) {
            return parameterized(field.getGenericType());
        } else {
            return parameterized(field.getType());
        }
    }

    private static Class parameterized(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return (Class) type;

    }

    public static String getIdFieldValue(@NonNull Object target) {
        Field targetField = ReflectionUtils.findField(target.getClass(), "id");
        ReflectionUtils.makeAccessible(targetField);
        try {
            log.debug("targetField[{}] class: {}",
                    targetField.get(target), targetField.get(target).getClass());
            return targetField.get(target).toString();
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
