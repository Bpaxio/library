package ru.otus.bbpax.configuration.changelog.data.builder;

import java.util.List;

public interface DataBuilder<E> {

    DataBuilder<E> defaultData();

    DataBuilder<E> with(E entity);

    List<E> build();
}
