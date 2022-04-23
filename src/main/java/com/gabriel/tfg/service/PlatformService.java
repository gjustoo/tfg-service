package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.Platform;

public interface PlatformService extends GenericService<Platform> {

    boolean platformExists(String name);

    Platform getByName(String name);
}
