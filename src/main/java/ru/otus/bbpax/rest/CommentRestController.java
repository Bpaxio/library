package ru.otus.bbpax.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.otus.bbpax.rest.exception.WrongRequestParamsException;
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.model.CommentDto;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("api/comment")
public class CommentRestController {

    private final CommentService service;

    @Autowired
    public CommentRestController(CommentService service) {
        this.service = service;
    }

    @PostMapping
    public void createComment(@RequestBody CommentDto commentDto) {
        if (Objects.isNull(commentDto)
                || Objects.isNull(commentDto.getUsername())
                || Objects.isNull(commentDto.getMessage())
                || Objects.isNull(commentDto.getBookId())
        ) throw new WrongRequestParamsException();

        service.create(
                commentDto.getUsername(),
                commentDto.getMessage(),
                commentDto.getBookId()
        );
    }

    @PutMapping("{id}")
    public void editComment(@PathVariable String id,
                            @RequestBody String message) {
        if (Objects.isNull(id) || Objects.isNull(message))
            throw new WrongRequestParamsException();

        service.update(id, message);
    }

    @GetMapping("{id}")
    @ResponseBody
    public CommentDto getComment(@PathVariable String id) {
        return service.getComment(id);
    }

    @GetMapping("/book/{id}")
    @ResponseBody
    public List<CommentDto> getBookComments(@PathVariable("id") String bookId) {
        if (Objects.isNull(bookId)) throw new WrongRequestParamsException();

        return service.getCommentsFor(bookId);
    }

    @GetMapping
    @ResponseBody
    public List<CommentDto> getComments() {
        return service.getAll();
    }
}
