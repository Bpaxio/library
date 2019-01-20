package ru.otus.bbpax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@EnableAutoConfiguration
@SpringBootApplication
public class LibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class);
    }
}
