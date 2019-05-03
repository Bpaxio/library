package ru.otus.bbpax.repository.security;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.security.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends MongoRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
