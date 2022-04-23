package com.gabriel.tfg.service;

import java.util.List;

public interface GenericService<T> {
    
    
    List<T> findAll();

    T save(T entity);

    T findById(long id);

    void delete(T entity);

    void deleteById(long id);

    long count();
}
