package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.User;

public interface UserService extends GenericService<User> {

    boolean emailExists(String email);

    User findByUsername(String name);

    User findByUsernameOrEmail(String username);

}
