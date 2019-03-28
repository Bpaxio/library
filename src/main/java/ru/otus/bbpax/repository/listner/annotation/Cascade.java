package ru.otus.bbpax.repository.listner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Cascade {
    CascadeType type() default CascadeType.ALL;

    /**
     * The collection the referred entity resides in.
     *
     * @return
     */
    String collection();
}
