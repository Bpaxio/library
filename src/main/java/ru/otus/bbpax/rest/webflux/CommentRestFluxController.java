package ru.otus.bbpax.rest.webflux;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.repository.reactive.BookReactiveRepo;
import ru.otus.bbpax.repository.reactive.CommentReactiveRepo;
import ru.otus.bbpax.rest.exception.WrongRequestParamsException;
import ru.otus.bbpax.service.model.CommentDto;

import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("flux/comment")
@AllArgsConstructor
public class CommentRestFluxController {
    private static final String NO_NAMED = "Anonymous";

    private final CommentReactiveRepo reactiveRepo;
    private final BookReactiveRepo bookReactiveRepo;

    @PostMapping
    @ResponseBody
    public Mono<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        if (Objects.isNull(commentDto)
                || Objects.isNull(commentDto.getMessage())
                || Objects.isNull(commentDto.getBookId())
        ) throw new WrongRequestParamsException();

        return bookReactiveRepo.findById(commentDto.getBookId())
                .flatMap(book -> reactiveRepo.save(
                        new Comment(commentDto.getUsername(), commentDto.getMessage(), book)))
                .map(CommentDto::fromEntity);
    }

    @PutMapping("{id}")
    @ResponseBody
    public Mono<CommentDto> editComment(@PathVariable String id,
                                        @RequestBody String message) {
        if (Objects.isNull(id) || Objects.isNull(message))
            throw new WrongRequestParamsException();

        return reactiveRepo.findById(id)
                .map(comment -> {
                    comment.setMessage(message);
                    return comment;
                })
                .flatMap(reactiveRepo::save)
                .map(CommentDto::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseBody
    public Mono<CommentDto> getComment(@PathVariable String id) {
        return reactiveRepo.findById(id)
                .map(CommentDto::fromEntity);
    }

    @GetMapping("/book/{id}")
    @ResponseBody
    public Flux<CommentDto> getBookComments(@PathVariable("id") String bookId) {
        if (Objects.isNull(bookId)) throw new WrongRequestParamsException();

        return reactiveRepo.findAllByBookId(bookId)
                .map(CommentDto::fromEntity);
    }

    @GetMapping
    @ResponseBody
    public Flux<CommentDto> getComments() {
        return reactiveRepo.findAll()
                .map(CommentDto::fromEntity);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteComment(@PathVariable String id) {
        return reactiveRepo.deleteById(id);
    }
}
