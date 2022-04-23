package com.gabriel.tfg.repository;

import com.gabriel.tfg.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long,User> {
    

    
    
}
