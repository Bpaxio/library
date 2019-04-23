package ru.otus.bbpax.configuration.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class CustomBigDecimalDeserializer extends StdDeserializer<BigDecimal> {

    public CustomBigDecimalDeserializer() {
        this(null);
    }

    public CustomBigDecimalDeserializer(Class<BigDecimal> t) {
        super(t);
    }

    @Override
    public BigDecimal deserialize(JsonParser jsonparser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        return new BigDecimal(date);
    }
}
