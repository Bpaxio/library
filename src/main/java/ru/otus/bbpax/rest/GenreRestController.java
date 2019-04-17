package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreView;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@RestController("api/genre")
@AllArgsConstructor
public class GenreRestController {

    private final GenreService service;

    @PostMapping
    public void createGenre(@RequestBody String name) {
        service.create(new GenreView(null, name));
    }

    @PutMapping("{id}")
    public void updateGenre(@PathVariable String id, @RequestBody String name) {
        service.update(new GenreView(id, name));
    }

    @GetMapping("{id}")
    public GenreView getGenre(@PathVariable String id) {
        return service.getGenreById(id);
    }

    @GetMapping
    public List<GenreView> getGenres() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteGenreById(@PathVariable String id) {
        service.deleteById(id);
    }
}
