package com.gabriel.tfg.service;

import com.gabriel.tfg.entity.ApiConnection;
import com.gabriel.tfg.entity.Platform;

public interface ApiConnectionService extends GenericService<ApiConnection> {

    ApiConnection getByPlatform(Platform platform);
    
    
}
