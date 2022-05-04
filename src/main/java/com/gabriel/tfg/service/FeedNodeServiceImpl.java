package com.gabriel.tfg.service;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.repository.FeedNodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedNodeServiceImpl extends GenericServiceImpl<FeedNode> implements FeedNodeService {

    @Autowired
    private FeedNodeRepository repository;

    public FeedNodeServiceImpl(FeedNodeRepository repo) {
        super(repo);
    }

    @Override
    public List<FeedNode> getAllByPlatform(Platform platform) {

        return repository.findAllByPlatform(platform);
    }

    @Override
    public boolean feedNodeExists(FeedNode feed) {

        FeedNode feedNode = repository.findOneByUidAndPlatform(feed.getUid(), feed.getPlatform());


        return feedNode != null; 
    }

}
