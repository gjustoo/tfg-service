package com.gabriel.tfg.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;
import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.service.PostService;
import com.gabriel.tfg.service.UserService;
import com.gabriel.tfg.utils.RedditUtils;
import com.gabriel.tfg.utils.RedditUtils.FILTER;
import com.gabriel.tfg.utils.TwitterUtils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Post controller", description = "Controller to manage posts")
@RestController
@RequestMapping("post")
public class PostController extends GenericController<Post> {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    public PostController(PostService service) {
        super(service);
    }

    @ApiOperation(value = "Get feed post from user", httpMethod = "GET", nickname = "GET_FEED_POSTS")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Post.class),
            @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/update/{username}")
    public ResponseEntity<Object> updateFeedFromUser(@PathVariable String username) {

        User user = userService.findByUsername(username);

        List<Post> posts = new ArrayList();
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        }

        List<FeedNode> feedNodes = user.getAllNodes();

        for (FeedNode feedNode : feedNodes) {
            if (feedNode.getPlatform().getName().equalsIgnoreCase("reddit")) {
                posts.addAll(RedditUtils.getPosts(feedNode, FILTER.MONTH));
            } else {
                posts.addAll(
                        TwitterUtils.getFeedFromUser(feedNode, LocalDateTime.now().minusDays(30), LocalDateTime.now()));
            }
        }

        Collections.shuffle(posts);
        postService.insertOrUpdateAll(posts);
        return ResponseEntity.ok(posts);

    }

    @ApiOperation(value = "Get feed post from user", httpMethod = "POST", nickname = "GET_FEED_POSTS")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Post.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/feed/{username}")
    public ResponseEntity<Object> getFeedFromUserBetweenDates(@PathVariable String username,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam("page") int page,
            @RequestParam("update") boolean update) {

        User user = userService.findByUsername(username);
        List<Post> posts = new ArrayList();

        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        }

        if (update) {
            List<Post> updatingPost = new ArrayList();
            List<FeedNode> feedNodes = user.getAllNodes();

            for (FeedNode feedNode : feedNodes) {
                if (feedNode.getPlatform().getName().equalsIgnoreCase("reddit")) {
                    updatingPost.addAll(RedditUtils.getPosts(feedNode, FILTER.MONTH));
                } else {
                    updatingPost.addAll(
                            TwitterUtils.getFeedFromUser(feedNode, LocalDateTime.now().minusDays(30),
                                    LocalDateTime.now()));
                }
            }
            Collections.shuffle(updatingPost);
            postService.insertOrUpdateAll(updatingPost);
        }

        List<FeedNode> feedNodes = user.getAllNodes();
        List<Post> postsFinal = postService.findAllBetweenDatesAndFeedNodeIn(start, end, feedNodes, page);
        List<Post> userLiked = user.getLikedPosts();
        Collections.shuffle(postsFinal);
        return ResponseEntity.ok(postsFinal.stream().map(e -> e.toJSON(userLiked.contains(e)).toMap()).collect(Collectors.toList()));

    }

    @ApiOperation(value = "Get feed post from user", httpMethod = "POST", nickname = "GET_FEED_POSTS")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Post.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/liked/{username}")
    public ResponseEntity<Object> getLikedFromUser(@PathVariable String username) {

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        }
        List<Post> posts = user.getLikedPosts();
        Collections.reverse(posts);
        return ResponseEntity.ok(posts.stream().map(e -> e.toJSON(true).toMap()).collect(Collectors.toList()));

    }

    @ApiOperation(value = "User likes a post", httpMethod = "POST", nickname = "Likes post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/likes_post/")
    public ResponseEntity<Object> userLikePost(@RequestBody(required = true) Map<String, String> data) {

        JSONObject json = new JSONObject(data);

        User user = userService.findByUsername(json.getString("username"));
        Post post = postService.get(Long.parseLong(json.getString("id")));

        if (user == null || post == null) {
            return ResponseEntity.badRequest().body("User or post does not exists");
        } else {

            if(!user.getLikedPosts().contains(post)){

                user.getLikedPosts().add(post);
            }else{
                user.getLikedPosts().remove(post);
                userService.save(user);
                 return ResponseEntity.status(420).body("You unliked this post");
            }
            userService.save(user);
            return ResponseEntity.ok("Liked " + post.getTitle());
        }

    }

}