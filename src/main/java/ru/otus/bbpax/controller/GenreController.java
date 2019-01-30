package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.bbpax.controller.model.GenreView;
import ru.otus.bbpax.service.GenreService;

import java.util.List;

import static org.springframework.shell.standard.ShellOption.NULL;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@ShellComponent("genre")
@AllArgsConstructor
public class GenreController {

    private final GenreService service;

    @ShellMethod(key = {"genre --create", "genre -c"}, value = "Genre creation")
    public void createGenre(@ShellOption(help = "Genre's name.") String name) {
        service.create(new GenreView(null, name));
    }

    @ShellMethod(key = {"genre --update", "genre -u"}, value = "Update genre with id")
    public void updateGenre(@ShellOption(defaultValue = NULL, help = "Genre's id.") Long id,
                             @ShellOption(help = "Genre's name.", defaultValue = NULL) String name) {
        service.update(new GenreView(id, name));
    }

    @ShellMethod(key = {"genre --get", "genre -g"}, value = "Get Genre with id")
    public GenreView getGenre(@ShellOption(help = "Genre's id.") Long id) {
        return service.getGenreById(id);
    }

    @ShellMethod(key = {"genre --list", "genre -l"}, value = "Show all Genres")
    public List<GenreView> getGenres() {
        return service.getAll();
    }

    @ShellMethod(key = {"genre --delete", "genre -d"}, value = "Delete Genre by id")
    public void deleteGenreById(@ShellOption(help = "Genre's id.") Long id) {
        service.deleteById(id);
    }
}
