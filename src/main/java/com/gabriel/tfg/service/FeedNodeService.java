package com.gabriel.tfg.service;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;

public interface FeedNodeService extends GenericService<FeedNode> {

    List<FeedNode> getAllByPlatform(Platform platform);
    
    boolean feedNodeExists(FeedNode feed);

    FeedNode findTopByUidAndPlatform(String uid, Platform platform);
    
}
