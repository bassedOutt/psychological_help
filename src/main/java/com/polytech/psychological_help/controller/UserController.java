package com.polytech.psychological_help.controller;

import com.polytech.psychological_help.entity.User;
import com.polytech.psychological_help.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize(value = "hasRole('ADMIN')")
    public List<User> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(auth.getAuthorities()));
        return userService.findAllUsers();
    }

    @GetMapping("/test")
    public String allok() {
        return "ok";
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public User createUser(@RequestBody User user) {
        return userService.insert(user);
    }

    @PutMapping
    @ResponseStatus(OK)
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping
    @ResponseStatus(OK)
    public void deleteUser(@RequestBody User user) {
        userService.delete(user.getId());
    }
}
