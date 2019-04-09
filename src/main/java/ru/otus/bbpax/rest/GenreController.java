package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreView;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
// @RestController("/genre")
@AllArgsConstructor
public class GenreController {

    private final GenreService service;

    @PostMapping
    public void createGenre(String name, Model model) {
        service.create(new GenreView(null, name));
    }

    @PutMapping("/${id}")
    public void updateGenre(String id, String name, Model model) {
        service.update(new GenreView(id, name));
    }

    @GetMapping("/${id}")
    public GenreView getGenre(String id, Model model) {
        return service.getGenreById(id);
    }

    @GetMapping(value = "genre")
    public List<GenreView> getGenres(Model model) {
        return service.getAll();
    }

    @DeleteMapping("/${id}")
    public void deleteGenreById(String id, Model model) {
        service.deleteById(id);
    }
}
