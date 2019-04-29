package ru.otus.bbpax.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.bbpax.service.CommentService;

import java.util.Optional;

@Slf4j
@Controller
public class CommentController {

    private static final String NO_NAMED = "Anonymous";
    private final CommentService service;

    @Autowired
    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping("{bookId}/comment")
    public String createComment(@PathVariable("bookId") String bookId,
                                String username,
                                String message
    ) {
        UserDetails user = getUser();
        if (username == null || username.isEmpty()) {
            username = Optional
                    .ofNullable(user.getUsername())
                    .orElse(NO_NAMED);
        }
        service.create(username, message, bookId);
        return "redirect:/book/" + bookId;
    }

    @PostMapping("{bookId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("bookId") String bookId,
                                @PathVariable("commentId") String commentId) {

        service.deleteById(commentId);
        return "redirect:/book/" + bookId;
    }

    private UserDetails getUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
