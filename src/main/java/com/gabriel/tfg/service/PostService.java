package com.gabriel.tfg.service;

import java.time.LocalDateTime;
import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;

public interface PostService extends GenericService<Post> {

    boolean postExists(Post post);

    List<Post> getAllByPlatform(FeedNode feedNode);

    void insertOrUpdateAll(List<Post> posts);

    void insertOrUpdate(Post post);

    List<Post> findAllBetweenDatesAndFeedNodeIn(LocalDateTime start, LocalDateTime end, List<FeedNode> nodes);

}
