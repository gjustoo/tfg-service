package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    @Autowired
    private UserRepository repository;

    public UserServiceImpl(UserRepository repo) {
        super(repo);
    }

    @Override
    public boolean emailExists(String email) {
        User user = repository.findOneByEmail(email);

        return user != null;
    }

}
