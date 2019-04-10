package ru.otus.bbpax.repository;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import ru.otus.bbpax.configuration.changelog.DataInitChangelog;
import ru.otus.bbpax.configuration.converter.BigDecimalConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoBeeConfig {
    public static final String CHANGELOG_COLLECTION_NAME = "test-changelog";
    public static final String CHANGELOG_LOCK_COLLECTION_NAME = "test-changelog-lock";

    @Autowired
    private MongoClient mongo;

    @Bean
    public Mongobee mongobee(Environment environment, MongoTemplate template) {
        Mongobee runner = new Mongobee(mongo);
        runner.setDbName("test-library");
        runner.setChangelogCollectionName(CHANGELOG_COLLECTION_NAME);
        runner.setLockCollectionName(CHANGELOG_LOCK_COLLECTION_NAME);
        runner.setChangeLogsScanPackage(DataInitChangelog.class.getPackage().getName());
        runner.setSpringEnvironment(environment);
        runner.setMongoTemplate(template);
        return runner;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new BigDecimalConverter());
        return new MongoCustomConversions(converters);
    }
}
