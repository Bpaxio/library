package ru.otus.bbpax.rest;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static ru.otus.bbpax.entity.security.Roles.ADMIN;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = {ADMIN})
public @interface WithMockAdmin {
}
