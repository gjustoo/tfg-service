package com.gabriel.tfg.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Post;
import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.service.PostService;
import com.gabriel.tfg.service.UserService;
import com.gabriel.tfg.utils.RedditUtils;
import com.gabriel.tfg.utils.RedditUtils.FILTER;
import com.gabriel.tfg.utils.TwitterUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

        postService.insertOrUpdateAll(posts);
        Collections.shuffle(posts);
        return ResponseEntity.ok(posts);

    }

    @ApiOperation(value = "Get feed post from user", httpMethod = "POST", nickname = "GET_FEED_POSTS")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = Post.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/feed/{username}")
    public ResponseEntity<Object> getFeedFromUserBetweenDates(@PathVariable String username,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        User user = userService.findByUsername(username);
        List<Post> posts = new ArrayList();

        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        }

        List<FeedNode> feedNodes = user.getAllNodes();

        posts.addAll(postService.findAllBetweenDatesAndFeedNodeIn(start, end, feedNodes));
        Collections.shuffle(posts);
        return ResponseEntity.ok(posts);

    }

}