package com.gabriel.tfg.repository;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;

import org.springframework.stereotype.Repository;

@Repository
public interface FeedNodeRepository extends GenericRepository<FeedNode> {

    List<FeedNode> findAllByPlatform(Platform platform);

    FeedNode findOneByUidAndPlatform(String uid, Platform platform);

}
