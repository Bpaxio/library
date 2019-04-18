package ru.otus.bbpax.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.CommentRepo;
import ru.otus.bbpax.service.CommentService;
import ru.otus.bbpax.service.error.NotFoundException;
import ru.otus.bbpax.service.model.CommentDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepo repo;
    @Autowired
    private BookRepo bookRepo;

    @Transactional
    public void create(String username, String text, String bookId) {
        Optional<Book> book = bookRepo.findById(bookId);
        if (!book.isPresent()) {
            throw new NotFoundException("Book", bookId);
        }
        repo.save(new Comment(username, text, book.get()));
    }

    @Transactional
    public void update(String id, String text) {
        repo.update(id, text);
    }

    @Transactional
    public List<CommentDto> getCommentsFor(String bookId) {
        return repo.findAllByBookId(bookId)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsBy(String username) {
        return repo.findAllByUsername(username)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAll() {
        return repo.findAll()
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    public CommentDto getComment(String id) {
        return CommentDto.fromEntity(repo.findById(id).get());
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}
