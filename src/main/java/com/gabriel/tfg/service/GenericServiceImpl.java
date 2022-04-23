package com.gabriel.tfg.service;

import java.util.List;

import com.gabriel.tfg.entity.GenericEntity;
import com.gabriel.tfg.repository.GenericRepository;

public abstract class GenericServiceImpl<T extends GenericEntity<T>> implements GenericService<T> {

    private final GenericRepository<T> repo;

    public GenericServiceImpl(GenericRepository<T> repo) {
        this.repo = repo;
    }

    @Override
    public List<T> findAll() {
        return repo.findAll();
    }

    @Override
    public T save(T entity) {
        return repo.save(entity);
    }

    @Override
    public T get(Long id) {
        return repo.findOneById(id);
    }

    @Override
    public void delete(T entity) {
        repo.delete(entity);

    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public T update(T entity) {
        return repo.save(entity);
    }

}
