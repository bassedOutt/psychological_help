package com.polytech.psychological_help.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.polytech.psychological_help.entity.Role;
import com.polytech.psychological_help.entity.User;
import com.polytech.psychological_help.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.polytech.psychological_help.util.Constants.ROLE_CLAIM;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Algorithm algorithm;

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

    @PostMapping("/add_role_to_user")
    public User addRoleToUser(@RequestBody String email, String role) {
        return userService.addRoleToUser(email, role);
    }

    @GetMapping("/token/refresh")
    @ResponseStatus(OK)
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String refresh_token = authorizationHeader.substring("Bearer ".length());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);
            String username = decodedJWT.getSubject();
            User user = userService.findByEmail(username);

            String access_token = JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim(ROLE_CLAIM, user.getRoles().stream()
                            .map(Role::getRoleName)
                            .collect(Collectors.toList()))
                    .sign(algorithm);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);
            return tokens;
        }
        return null;
    }
}
