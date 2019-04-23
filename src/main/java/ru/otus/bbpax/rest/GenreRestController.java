package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreDto;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@RestController
@CrossOrigin
@RequestMapping("api/genre")
@AllArgsConstructor
public class GenreRestController {

    @Autowired
    private GenreService service;

    @PostMapping
    public void createGenre(@RequestBody String name) {
        service.create(new GenreDto(null, name));
    }

    @PutMapping("{id}")
    public void updateGenre(@PathVariable String id, @RequestBody String name) {
        service.update(new GenreDto(id, name));
    }

    @GetMapping("{id}")
    @ResponseBody
    public GenreDto getGenre(@PathVariable String id) {
        return service.getGenreById(id);
    }

    @GetMapping
    @ResponseBody
    public List<GenreDto> getGenres() {
        return service.getAll();
    }

    @DeleteMapping("{id}")
    public void deleteGenreById(@PathVariable String id) {
        service.deleteById(id);
    }
}
