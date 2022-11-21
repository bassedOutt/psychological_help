package com.polytech.psychological_help.service;

import com.polytech.psychological_help.entity.Role;
import com.polytech.psychological_help.entity.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User getCurrentUser();

    List<User> findAllUsers();

    User addRoleToUser(String email, String rolename);

    User insert(User user);

    User update(User entity);

    Role saveRole(String roleName);

    void delete(Integer id);
}
