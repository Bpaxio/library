package ru.otus.bbpax.configuration.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.entity.security.User;

import java.util.Collections;

import static ru.otus.bbpax.configuration.changelog.data.ChangelogDataFactory.defaultAuthors;
import static ru.otus.bbpax.configuration.changelog.data.ChangelogDataFactory.defaultBooks;
import static ru.otus.bbpax.configuration.changelog.data.ChangelogDataFactory.defaultComments;
import static ru.otus.bbpax.configuration.changelog.data.ChangelogDataFactory.defaultGenres;
import static ru.otus.bbpax.entity.security.Roles.ADMIN;
import static ru.otus.bbpax.entity.security.Roles.USER;

@Slf4j
@ChangeLog
public class DataInitChangelog {

    @ChangeSet(order = "001", id = "addAuthors", author = "bpaxio")
    public void addAuthors(MongoTemplate template) {
        template.insertAll(defaultAuthors());
    }

    @ChangeSet(order = "002", id = "addGenres", author = "bpaxio")
    public void addGenres(MongoTemplate template) {
        template.insertAll(defaultGenres());
    }

    @ChangeSet(order = "003", id = "addBooks", author = "bpaxio")
    public void addBooks(MongoTemplate template) {
        template.insertAll(defaultBooks(template));
    }

    @ChangeSet(order = "004", id = "addComments", author = "bpaxio")
    public void addComments(MongoTemplate template) {
        Book book1 = template.findById("1c77bb3f57cfe05a39abc17a", Book.class);
        Book book2 = template.findById("3c77bb3f57cfe05a39abc17a", Book.class);
        template.insertAll(defaultComments(book1, book2));

        template.save(book1);
        template.save(book2);
    }

    @ChangeSet(order = "005", id = "addUsers", author = "bpaxio")
    public void addUsers(MongoTemplate template) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("$2a$10$jHC1UIWnP8DHAvOBZ9SU4ujETh6Xm1yECXE6iFmZpbBnBiaR3J/J6");
        admin.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + ADMIN)));
        template.insert(admin, "users");

        User user = new User();
        user.setUsername("user");
        user.setPassword("$2a$10$1nHcrt0Iq1BQClOdWgrF7e0.pkpmipzmyBGKZMerMMXlr0CFnfpHG");
        user.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + USER)));
        template.insert(user, "users");
    }
}
