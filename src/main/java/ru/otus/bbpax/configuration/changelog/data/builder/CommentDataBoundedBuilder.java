package ru.otus.bbpax.configuration.changelog.data.builder;

import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentDataBoundedBuilder implements DataBuilder<Comment> {

    private final EntryBounder bounder;

    private final List<Comment> comments;

    public CommentDataBoundedBuilder() {
        bounder = new EntryBounder();
        comments = new ArrayList<>();
    }

    @Override
    public DataBuilder<Comment> defaultData() {
        return this;
    }

    @Override
    public DataBuilder<Comment> with(Comment entity) {
        comments.add(entity);
        return this;
    }

    @Override
    public List<Comment> build() {
        if (bounder.ready()) {
            bounder.boundAndGetResult()
                    .forEach(this::saveResult);
            bounder.reset();
        }
        return comments;
    }

    public CommentDataBoundedBuilder forBook(Book book) {
        try {
            bounder.forBook(book);
        } catch (IllegalStateException e) {
            bounder.boundAndGetResult()
                    .forEach(this::saveResult);

            bounder.forAnotherBook(book);
        }
        return this;
    }

    public CommentDataBoundedBuilder makeComment(Comment comment) {
        bounder.makeComment(comment);
        return this;
    }

    private void saveResult(Comment comment) {
        comments.add(comment);
    }

    private static class EntryBounder {
        private Book book;
        private List<Comment> comments = new ArrayList<>();

        EntryBounder forAnotherBook(Book book) {
            reset().forBook(book);
            return this;
        }

        void forBook(Book book) {
            if (this.book != null) {
                throw new IllegalStateException("Bounder should be cleared!");
            }
            this.book = book;
        }

        EntryBounder makeComment(Comment comment) {
            this.comments.add(comment);
            return this;
        }

        boolean ready() {
            return Objects.nonNull(book) && !comments.isEmpty();
        }

        EntryBounder reset() {
            this.book = null;
            this.comments.clear();
            return this;
        }

        List<Comment> boundAndGetResult() {
            return bound().getResult();
        }

        EntryBounder bound() {
            if(Objects.nonNull(book)) {
                this.book.setComments(comments);
                this.comments.forEach(comment -> comment.setBook(book));
            }
            return this;
        }

        List<Comment> getResult() {
            return comments;
        }
    }
}
