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
    private static final String NO_NAMED = "Anonymous";

    private final CommentService service;

    @Autowired
    public CommentRestController(CommentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseBody
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        if (Objects.isNull(commentDto)
                || Objects.isNull(commentDto.getMessage())
                || Objects.isNull(commentDto.getBookId())
        ) throw new WrongRequestParamsException();


        return service.create(
                Objects.isNull(commentDto.getUsername()) ? NO_NAMED : commentDto.getUsername(),
                commentDto.getMessage(),
                commentDto.getBookId()
        );
    }

    @PutMapping("{id}")
    @ResponseBody
    public CommentDto editComment(@PathVariable String id,
                            @RequestBody String message) {
        if (Objects.isNull(id) || Objects.isNull(message))
            throw new WrongRequestParamsException();

        return service.update(id, message);
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

    @DeleteMapping("{id}")
    @ResponseBody
    public void deleteComment(@PathVariable String id) {
        service.deleteById(id);
    }
}
