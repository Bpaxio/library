package ru.otus.bbpax.controller;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.bbpax.controller.model.AuthorView;
import ru.otus.bbpax.service.AuthorService;

import java.util.List;

import static org.springframework.shell.standard.ShellOption.NULL;

@AllArgsConstructor
@ShellComponent("author")
public class AuthorController {

    private final AuthorService service;

    @ShellMethod(key = {"author -create", "author -c"}, value = "author creation")
    public void createAuthor(@ShellOption(help = "Author's name.") String name,
                             @ShellOption(help = "Author's surname.") String surname,
                             @ShellOption(help = "Country, where the author was born.") String country) {
        service.create(new AuthorView(null, name, surname, country));
    }

    @ShellMethod(key = {"author --update", "author -u"}, value = "update author with id")
    public void updateAuthor(@ShellOption(help = "Author's id.") String id,
                             @ShellOption(help = "Author's name.", defaultValue = NULL) String name,
                             @ShellOption(help = "Author's surname.", defaultValue = NULL)  String surname,
                             @ShellOption(help = "Country, where the author was born.", defaultValue = NULL) String country) {
        service.update(new AuthorView(id, name, surname, country));
    }

    @ShellMethod(key = {"author --get", "author -g"}, value = "get Author with id")
    public AuthorView getAuthor(@ShellOption(help = "Author's id.") String id) {
        return service.getAuthorById(id);
    }

    @ShellMethod(key = {"author --list", "author -l"}, value = "Show all Authors")
    public List<AuthorView> getAuthors() {
        return service.getAll();
    }

    @ShellMethod(key = {"author --delete", "author -d"}, value = "delete Author by id")
    public void deleteAuthorById(@ShellOption(help = "Author's id.") String id) {
        service.deleteById(id);
    }
}
