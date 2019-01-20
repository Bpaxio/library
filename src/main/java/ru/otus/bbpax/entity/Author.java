package ru.otus.bbpax.entity;

import lombok.Data;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
public class Author {
    private Long id;
    private String name;
    private String surname;
    private String country;
}
