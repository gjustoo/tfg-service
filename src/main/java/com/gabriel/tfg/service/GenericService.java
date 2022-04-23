package com.gabriel.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

public interface GenericService<T> {
    
    
    List<T> findAll();

    T save(T entity);

    T get(Long id);

    void delete(T entity);

    void delete(Long id);

    T update(T entity);
}
