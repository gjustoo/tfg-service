package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> {

    @Autowired
    private UserRepository repository;

    public UserServiceImpl(UserRepository repo) {
        this.repository = repo;
    }


    
    

}
