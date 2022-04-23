package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.User;

import org.springframework.stereotype.Service;

public interface UserService extends  GenericService<User>{



    boolean emailExists(String email);




    
    
}
