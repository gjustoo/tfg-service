package com.gabriel.tfg.service;

import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;
import com.gabriel.tfg.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends GenericServiceImpl<Post> implements PostService {

    @Autowired
    private PostRepository repository;

    public PostServiceImpl(PostRepository repo) {
        super(repo);
    }

    @Override
    public boolean postExists(Post post) {
        Post existing = repository.findOneByMediaUrl(post.getMediaUrl());

        return existing != null;
    }

    @Override
    public List<Post> getAllByPlatform(FeedNode feedNode) {
        return this.repository.findAllByFeedNode(feedNode);
    }

    @Override
    public void insertOrUpdate(Post post) {

        Post postToUpdate = repository.findOneByMediaUrl(post.getMediaUrl());

        if (postToUpdate != null) {

            postToUpdate.setBody(post.getBody());
            postToUpdate.setLikes(post.getLikes());
            postToUpdate.setFeedNode(post.getFeedNode());
            postToUpdate.setPostedDate(post.getPostedDate());
            postToUpdate.setSource(post.getSource());
            postToUpdate.setTitle(post.getTitle());

            repository.save(postToUpdate);
        } else {
            repository.save(post);
        }
    }

    @Override
    public void insertOrUpdateAll(List<Post> posts) {
        for (Post post : posts) {
            insertOrUpdate(post);
        }
    }

}
