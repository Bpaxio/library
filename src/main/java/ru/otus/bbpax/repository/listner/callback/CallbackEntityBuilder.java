package ru.otus.bbpax.repository.listner.callback;

import org.bson.Document;

import java.lang.reflect.Field;

public interface CallbackEntityBuilder {

    CallbackEntityBuilder forField(Field field);

    CallbackEntityBuilder forSource(Document source);

    CallbackEntityBuilder forSourceCollection(String sourceCollection);

    CallbackEntity buildCallbackEntity();
}
