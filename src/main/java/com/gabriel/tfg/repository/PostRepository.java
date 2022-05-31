package com.gabriel.tfg.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends GenericRepository<Post> {

    Post findTopByMediaUrl(String mediaUrl);

    List<Post> findAllByFeedNode(FeedNode feedNode);

    List<Post> findAllByPostedDateBetweenAndFeedNodeIn(LocalDateTime start, LocalDateTime end, List<FeedNode> nodes);
    List<Post> findAllByPostedDateBetweenAndFeedNodeIn(LocalDateTime start, LocalDateTime end, List<FeedNode> nodes,Pageable pageable);


}
