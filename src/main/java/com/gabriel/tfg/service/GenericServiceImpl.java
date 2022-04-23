package com.gabriel.tfg.service;

import java.util.List;

import com.gabriel.tfg.repository.GenericRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class GenericServiceImpl<T> implements GenericService<T> {

    @Autowired
    private GenericRepository<T> repo;

    @Override
    public List<T> findAll() {
        return repo.findAll();
    }

    @Override
    public T save(T entity) {
        return repo.save(entity);
    }

    @Override
    public T findById(long id) {
        // TODO Auto-generated method stub
        return repo.findById(id);
    }

    @Override
    public void delete(T entity) {
        repo.delete(entity);

    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public long count() {

        return 0;
    }

}
