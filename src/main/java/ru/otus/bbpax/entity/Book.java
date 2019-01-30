package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private Long id;
    private String name;
    private Integer publicationDate;
    private String publishingOffice;
    private BigDecimal price;

    private Genre genre;
    private Author author;
}
