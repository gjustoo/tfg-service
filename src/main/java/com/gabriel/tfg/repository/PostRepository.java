package com.gabriel.tfg.repository;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;

import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends GenericRepository<Post> {

    Post findOneByMediaUrl(String mediaUrl);

    List<Post> findAllByFeedNode(FeedNode feedNode);

}