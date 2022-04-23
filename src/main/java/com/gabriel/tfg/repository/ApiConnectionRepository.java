package com.gabriel.tfg.repository;

import com.gabriel.tfg.entity.ApiConnection;
import com.gabriel.tfg.entity.Platform;

import org.springframework.stereotype.Repository;

@Repository
public interface ApiConnectionRepository extends GenericRepository<ApiConnection> {

    ApiConnection findOneByPlatform(Platform platform);

}
