package com.gabriel.tfg.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class User extends GenericEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String passwd;

    private LocalDateTime creationDate;

    @ManyToMany
    private List<User> following;

    @ManyToMany
    private List<FeedNode> followingNodes;

    @ManyToMany
    private List<Post> likedPosts;

}
