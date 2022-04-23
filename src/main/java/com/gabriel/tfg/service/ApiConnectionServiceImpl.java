package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.ApiConnection;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.repository.ApiConnectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiConnectionServiceImpl extends GenericServiceImpl<ApiConnection> implements ApiConnectionService {

    @Autowired
    private ApiConnectionRepository repository;

    public ApiConnectionServiceImpl(ApiConnectionRepository repo) {
        super(repo);
    }

    @Override
    public ApiConnection getByPlatform(Platform platform) {

        return repository.findOneByPlatform(platform);
    }

}
