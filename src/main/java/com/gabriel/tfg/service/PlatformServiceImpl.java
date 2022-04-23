package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.repository.PlatformRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformServiceImpl extends GenericServiceImpl<Platform> implements PlatformService {

    @Autowired
    private PlatformRepository repository;

    public PlatformServiceImpl(PlatformRepository repo) {
        super(repo);
    }

    @Override
    public boolean platformExists(String name) {
        Platform user = repository.findOneByName(name);

        return user != null;
    }

    @Override
    public Platform getByName(String name) {
        return this.repository.findOneByName(name);
    }

}
