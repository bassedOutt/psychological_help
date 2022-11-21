package com.polytech.psychological_help.service.impl;

import com.polytech.psychological_help.entity.Role;
import com.polytech.psychological_help.entity.User;
import com.polytech.psychological_help.repository.RoleRepository;
import com.polytech.psychological_help.repository.UserRepository;
import com.polytech.psychological_help.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    @Override
    public List<User> findAllUsers() {
        log.info("fetching all users");
        return repository.findAll();
    }

    @Override
    public User addRoleToUser(String email, String rolename) {
        log.info("adding role {} to user {}", rolename, email);
        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByRoleName(rolename);
        user.addRole(role);
        repository.save(user);
        return user;
    }

    @Override
    public User insert(User user) {
        log.info("saving user {} to database", user.getEmail());
        repository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("updating user: {}", user.getEmail());
        repository.save(user);
        return user;
    }

    @Override
    public Role saveRole(String roleName) {
        Role role = new Role(roleName);
        roleRepository.save(role);
        return role;
    }

    @Override
    public void delete(Integer id) {
        log.info("deleting user with an id: {}", id);
        repository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        log.info("Searching for user with email: {}", email);
        return repository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        log.info("User in security context: {}", principal);

        return repository.findByEmail(principal).orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.findByEmail(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with email %s not found", s)));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), "", authorities);
    }
}