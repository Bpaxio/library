package ru.otus.bbpax.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long id;
    private String name;
    private String surname;
    private String country;
}
