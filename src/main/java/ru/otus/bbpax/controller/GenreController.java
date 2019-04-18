package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.GenreService;
import ru.otus.bbpax.service.model.GenreDto;

import static ru.otus.bbpax.controller.Templates.*;

@Controller
@AllArgsConstructor
public class GenreController {

    private final GenreService service;

    @PostMapping("/genre")
    public String createGenre(String name, Model model) {
        model.addAttribute("genre", GenreDto.fromEntity(service.create(new GenreDto(null, name))));
        return GENRE;
    }

    @PostMapping("/genre/{id}")
    public String updateGenre(@PathVariable("id") String id, String name) {
        service.update(new GenreDto(id, name));
        return "redirect:" + id;
    }

    @PostMapping("/genre/{id}/delete")
    public String deleteGenreById(@PathVariable("id") String id, Model model) {
        service.deleteById(id);
        return getAllGenres(model);
    }

    @GetMapping("/genre/{id}")
    public String getGenre(@PathVariable String id,
                           @RequestParam(value = "action", required = false) String action,
                           Model model
    ) {
        model.addAttribute("genre", service.getGenreById(id));
        if ("edit".equals(action)) {
            return GENRE_EDIT;
        }
        return GENRE;
    }

    @GetMapping("/genre")
    public String getGenres(@RequestParam(value = "action", required = false) String action,
                             Model model
    ) {
        if ("create".equals(action)) {
            return GENRE_CREATE;
        }
        return getAllGenres(model);
    }

    private String getAllGenres(Model model) {
        model.addAttribute("genres", service.getAll());
        return GENRES;
    }
}
