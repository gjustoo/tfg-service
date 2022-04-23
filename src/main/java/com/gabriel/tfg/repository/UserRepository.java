package com.gabriel.tfg.repository;

import com.gabriel.tfg.entity.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {

    User findOneByEmail(String email);

}
