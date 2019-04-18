package ru.otus.bbpax.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.model.CommentDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/comment")
public class CommentRestController {

    private final CommentService service;

    @Autowired
    public CommentRestController(CommentService service) {
        this.service = service;
    }

    @PostMapping
    public void createComment(String username,
                              String bookId,
                              String message) {
        service.create(username, message, bookId);
    }

    @PutMapping("{id}")
    public void editComment(@PathVariable String id,
                            @RequestBody String message) {
        service.update(id, message);
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommentDto getComment(@PathVariable String id) {
        return service.getComment(id);
    }

    @GetMapping("/book/{id}")
    @ResponseBody
    public List<CommentDto> getBookComments(@PathVariable String bookId) {
        return service.getCommentsFor(bookId);
    }

    @GetMapping
    @ResponseBody
    public List<CommentDto> getCommentsByUser(@RequestBody String username) {
        return service.getCommentsBy(username);
    }

    @GetMapping
    @ResponseBody
    public List<CommentDto> getComments() {
        return service.getAll();
    }
}
