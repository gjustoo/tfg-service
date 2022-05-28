package com.gabriel.tfg.entity;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.json.JSONObject;

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

    public List<FeedNode> getAllNodes() {
        Set<FeedNode> result = new HashSet();

        result.addAll(followingNodes);

        for (User user : following) {
            result.addAll(user.followingNodes);
        }

        return new ArrayList<FeedNode>(result);
    }

    public JSONObject getJson() {
        JSONObject result = new JSONObject();
        result.put("username", this.name);
        result.put("email", this.email);
        result.put("creation_date", this.creationDate);
        result.put("formatted_date", this.creationDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
        result.put("users_following", this.following.size());
        result.put("pages_following", this.followingNodes.size());
        result.put("liked_posts_count", this.likedPosts.size());
        return result;

    }

}
