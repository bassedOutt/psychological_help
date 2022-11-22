package com.polytech.psychological_help.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polytech.psychological_help.entity.User;
import com.polytech.psychological_help.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthorizationFilter implements Filter {
    public static final String BEARER = "Bearer ";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    private static final String ROLE_CLAIM = "roles";
    private final Algorithm algorithm;

    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            try {
                String token = authorizationHeader.substring(BEARER.length());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getClaim(EMAIL).asString();
                String[] roles = decodedJWT.getClaim(ROLE_CLAIM).asArray(String.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                saveOrUpdateUser(decodedJWT);
                if (roles != null) {
                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, "", authorities);

                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authenticationToken);
                HttpSession session = request.getSession(true);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

            } catch (Exception ex) {
                log.error("Error logging in : {}", ex.getMessage());
                response.setHeader("error", ex.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> tokens = new HashMap<>();
                tokens.put("error_message", ex.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }

        } else {
            Map<String, String> tokens = new HashMap<>();
            tokens.put("error_message", "user is not authorized");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }
        filterChain.doFilter(request, response);
    }

    private void saveOrUpdateUser(DecodedJWT decodedJWT) {
        String email = decodedJWT.getClaim(EMAIL).asString();
        String name = decodedJWT.getClaim(NAME).asString();
        String surname = decodedJWT.getClaim(SURNAME).asString();
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setLastName(surname);
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            userRepository.save(user);
        }
    }
}
