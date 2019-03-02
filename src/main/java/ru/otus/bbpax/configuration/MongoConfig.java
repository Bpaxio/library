package ru.otus.bbpax.configuration;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import ru.otus.bbpax.configuration.converter.BigDecimalConverter;
import ru.otus.bbpax.repository.listner.CascadeLoadMongoEventListener;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public Mongobee mongobee(MongoClient mongo, Environment environment, MongoTemplate template) {
        Mongobee runner = new Mongobee(mongo);
        runner.setDbName("library");
        runner.setChangeLogsScanPackage(DatabaseChangelog.class.getPackage().getName());
        runner.setSpringEnvironment(environment);
        // TODO: 2019-03-02 this will fix some problems
        runner.setMongoTemplate(template);
        return runner;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new BigDecimalConverter());
        return new MongoCustomConversions(converters);
    }

    // TODO: 2019-03-01 It can be used, but not now...
    @Bean
    public CascadeLoadMongoEventListener loadListener(MongoOperations mongoOps) {
        return new CascadeLoadMongoEventListener(mongoOps);
    }
}
