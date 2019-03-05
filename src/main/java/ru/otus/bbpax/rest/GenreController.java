package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.controller.model.GenreView;
import ru.otus.bbpax.service.GenreService;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@AllArgsConstructor
@RestController
public class GenreController {

    private final GenreService service;

    @PostMapping(value = "genre")
    public void createGenre(String name) {
        service.create(new GenreView(null, name));
    }

    @PutMapping(value = "genre")
    public void updateGenre(String id, String name) {
        service.update(new GenreView(id, name));
    }

    @GetMapping(value = "genre")
    public GenreView getGenre(String id) {
        return service.getGenreById(id);
    }

    @GetMapping(value = "genre")
    public List<GenreView> getGenres() {
        return service.getAll();
    }

    @PutMapping(value = "genre")
    public void deleteGenreById(String id) {
        service.deleteById(id);
    }
}
