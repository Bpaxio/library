package ru.otus.bbpax.controller.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.bbpax.entity.security.User;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class SecurityUtils {

    public static User getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new IllegalStateException("principal is not ready. It's not a User instance");
    }

    public static Collection<String> getRoles() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.info("getRoles catch exception: {} - {}", e.getClass(), e.getMessage());
        }
        return Collections.emptyList();
    }

    public static boolean getAuthenticated() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        } catch (NullPointerException e) {
            log.info("getAuthenticated catch exception: {} - {}", e.getClass(), e.getMessage());
            return false;
        }
    }

    public static String getUsername() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return userDetails.getUsername();
            }
        } catch (NullPointerException e) {
            log.info("getUsername catch exception: {} - {}", e.getClass(), e.getMessage());
        }
        return "Anonymous";
    }
}
