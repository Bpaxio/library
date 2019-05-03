package ru.otus.bbpax.rest;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.BookDto;
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
    @ResponseBody
    public GenreDto createGenre(@RequestBody String name) {
        return service.create(new GenreDto(null, name));
    }

    @PutMapping("{id}")
    @ResponseBody
    public GenreDto updateGenre(@PathVariable String id, @RequestBody String name) {
        return service.update(new GenreDto(id, name));
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

    @GetMapping("{id}/book")
    @ResponseBody
    public List<BookDto> getBooks(@PathVariable String id) {
        return service.getBooksById(id);
    }
}
