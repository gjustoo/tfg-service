package com.gabriel.tfg.controller;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.service.FeedNodeService;
import com.gabriel.tfg.service.PlatformService;
import com.gabriel.tfg.utils.TwitterUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "FeedNode controller", description = "Controller to manage FeedNodes")
@RestController
@RequestMapping("FeedNodes")
public class FeedNodeController extends GenericController<FeedNode> {

    @Autowired
    private FeedNodeService feedNodeService;

    @Autowired
    private PlatformService platformService;

    public FeedNodeController(FeedNodeService service) {
        super(service);
    }

    @ApiOperation(value = "Create new Feed Node", httpMethod = "POST", nickname = "create feed node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/")
    public ResponseEntity<Object> createFeedNode(@RequestParam(required = true) String name,
            @RequestParam(required = true) String uid,
            @RequestParam(required = true) Long platformId) {

        Platform platform = platformService.get(platformId);

        if (platform == null) {
            return ResponseEntity.badRequest().body("Platform not found");
        }

        FeedNode feedNode = FeedNode.builder().name(name).uid(uid).build();

        if (feedNodeService.feedNodeExists(feedNode)) {
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            this.feedNodeService.save(feedNode);
            return ResponseEntity.ok("ok");
        }

    }

    @ApiOperation(value = "Create new Feed Node", httpMethod = "POST", nickname = "create feed node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/userNode")
    public ResponseEntity<Object> createFeedByUsername(
            @RequestParam(required = true) String username,
            @RequestParam(required = true) Long platformId) {

        Platform platform = platformService.get(platformId);

        if (platform == null) {
            return ResponseEntity.badRequest().body("Platform not found");
        }

        FeedNode feedNode = TwitterUtils.getTwitterFeedByUserName(username, platform);

        if (feedNodeService.feedNodeExists(feedNode)) {
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            this.feedNodeService.save(feedNode);
            return ResponseEntity.ok("ok");
        }

    }

}