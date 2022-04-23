package com.gabriel.tfg.repository;

import com.gabriel.tfg.entity.Platform;

import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends GenericRepository<Platform> {

    Platform findOneByName(String name);

}
