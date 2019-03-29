package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreView;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@RestController
@AllArgsConstructor
public class GenreController {

    private final GenreService service;

    @PostMapping(value = "genre")
    public void createGenre(String name) {
        service.create(new GenreView(null, name));
    }

    @PutMapping("/${id}")
    public void updateGenre(String id, String name) {
        service.update(new GenreView(id, name));
    }

    @GetMapping("/${id}")
    public GenreView getGenre(String id) {
        return service.getGenreById(id);
    }

    @GetMapping(value = "genre")
    public List<GenreView> getGenres() {
        return service.getAll();
    }

    @DeleteMapping("/${id}")
    public void deleteGenreById(String id) {
        service.deleteById(id);
    }
}
