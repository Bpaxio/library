package ru.otus.bbpax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@SpringBootApplication
@EnableMongoRepositories
public class LibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class);
    }
}
