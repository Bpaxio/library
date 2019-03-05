package ru.otus.bbpax.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.bbpax.controller.model.CommentView;
import ru.otus.bbpax.service.CommentService;

import java.util.List;

@Slf4j
@RestController
public class CommentController {

    private final CommentService service;

    @Autowired
    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping(value = "comment")
    public void createComment(String username,
                              String bookId,
                              String text) {
        service.create(username, text, bookId);
    }

    @PutMapping(value = "comment")
    public void editComment(String id,
                            String text) {
        service.update(id, text);
    }

    @GetMapping(value = "comment")
    public CommentView getComment(String id) {
        return service.getComment(id);
    }

    @GetMapping(value = "comment")
    public List<CommentView> getBookComments(String bookId) {
        return service.getCommentsFor(bookId);
    }

    @GetMapping(value = "comment")
    public List<CommentView> getCommentsByUser(String username) {
        return service.getCommentsBy(username);
    }

    @GetMapping(value = "comment")
    public List<CommentView> getComments() {
        return service.getAll();
    }
}
