package ru.otus.bbpax.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.bbpax.controller.model.CommentView;
import ru.otus.bbpax.service.CommentService;

import java.util.List;
import java.util.Objects;

@Slf4j
@ShellComponent("comment")
public class CommentController {

    private final CommentService service;

    private String username;

    @Autowired
    public CommentController(CommentService service) {
        this.service = service;
    }

    @ShellMethod("hello")
    public String hello(@ShellOption(value = {"", "-n", "--name"}) String name) {
        username = name;
        return "Hi, " + name;
    }

    @ShellMethod(key = {"comment -c", "comment --create"}, value = "Add new comment to a book")
    @ShellMethodAvailability(value = "writeCommentAvailability")
    public void createComment(@ShellOption(help = "Book's id.") String bookId,
                              @ShellOption(help = "Your comment.") String text) {
        service.create(username, text, bookId);

    }

    @ShellMethod(key = {"comment -e", "comment --edit"}, value = "Update an existing comment")
    @ShellMethodAvailability(value = "writeCommentAvailability")
    public void editComment(@ShellOption(help = "Comment's id.") String id,
                            @ShellOption(help = "Your new comment.") String text) {
        service.update(id, text);
    }

    @ShellMethod(key = {"comment -l -b", "comment --list -b"}, value = "Show all comments to specified book")
    public List<CommentView> getBookComments(@ShellOption(help = "Book's id.") String bookId) {
        return service.getCommentsFor(bookId);
    }

    @ShellMethod(key = {"comment -l -u", "comment --list -u"}, value = "Show all comments to all books were added by specified user")
    public List<CommentView> getCommentsByUser(
            @ShellOption(help = "Username of comments' author.") String username) {
        return service.getCommentsBy(username);
    }

    @ShellMethod(key = {"comment -l", "comment --list"}, value = "Show list of all comments in the system")
    public List<CommentView> getComments() {
        return service.getAll();
    }

    public Availability writeCommentAvailability() {
        return Objects.nonNull(username)
                ? Availability.available()
                : Availability.unavailable("It is unavailable without greetings. "
                + "Call 'hello' first");
    }
}
