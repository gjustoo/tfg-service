package com.gabriel.tfg.service;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;

public interface PostService extends GenericService<Post> {

    boolean postExists(Post post);

    public List<Post> getAllByPlatform(FeedNode feedNode);

    public void insertOrUpdateAll(List<Post> posts);

    void insertOrUpdate(Post post);
}
