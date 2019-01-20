package ru.otus.bbpax.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
public class Book {

    private Long id;

    private String name;

    private LocalDateTime publicationDate;

    private BigDecimal price;

    private Genre genre;

    private Author author;
}
