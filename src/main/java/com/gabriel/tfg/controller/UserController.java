package com.gabriel.tfg.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.stream.Collectors;

import com.gabriel.tfg.entity.FeedNode;
import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.service.FeedNodeService;
import com.gabriel.tfg.service.UserService;
import com.gabriel.tfg.utils.ImageUtils;
import com.gabriel.tfg.utils.PasswordUtils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "User controller", description = "Controller to manage users")
@RestController
@RequestMapping("users")
public class UserController extends GenericController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedNodeService feedNodeService;

    public UserController(UserService service) {
        super(service);
    }

    @ApiOperation(value = "Creates user", httpMethod = "POST", nickname = "NEW_USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody(required = true) Map<String, String> data) {

        JSONObject object = new JSONObject(data);

        String name = object.getString("username");
        String email = object.getString("email");
        String passwd = object.getString("passwd");

        LocalDateTime creationTime = LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();

        String hashedpasswd = PasswordUtils.hash(passwd);

        User user = User.builder().name(name).email(email).passwd(hashedpasswd).creationDate(creationTime).build();

        if (userService.emailExists(email) || userService.findByUsername(name) != null) {
            return ResponseEntity.badRequest().body("Email or username already taken");
        } else {
            this.userService.save(user);
            return ResponseEntity.ok("ok");
        }

    }

    @ApiOperation(value = "Logs in a user", httpMethod = "POST", nickname = "Log_USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/logIn")
    public ResponseEntity<Object> logIn(@RequestBody(required = true) Map<String, String> data) {

        JSONObject object = new JSONObject(data);

        String username = object.getString("username");
        String passwd = object.getString("passwd");

        User user = userService.findByUsernameOrEmail(username);

        if (user != null) {

            boolean valid = PasswordUtils.test(user.getPasswd(), passwd);

            if (valid) {
                return ResponseEntity.ok().body("Logged in");
            } else {
                return ResponseEntity.badRequest().body("Password does not match");
            }

        } else {
            return ResponseEntity.badRequest().body("this username does not exist");
        }

    }

    @ApiOperation(value = "GETs user by name", httpMethod = "GET", nickname = "GET_USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS"),
            @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/user/{username}")
    public ResponseEntity<Object> createUser(@PathVariable(required = true) String username) {

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        } else {

            return ResponseEntity.ok(user.toResponse().toMap());
        }

    }

    @ApiOperation(value = "Create new Feed Node", httpMethod = "GET", nickname = "create feed node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/following_pages/{username}")
    public ResponseEntity<Object> getPagesByUsername(@PathVariable(required = true) String username) {

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        } else {

            return ResponseEntity.ok(user.getFollowingNodes().stream().map(FeedNode::getJSON)
                    .map(kong.unirest.json.JSONObject::toMap).collect(Collectors.toList()));
        }

    }


    @ApiOperation(value = "UNFOLLOW Feed Node", httpMethod = "DELETE", nickname = "UNFOLLOW feed node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @DeleteMapping("/{username}/unfollow/{node}")
    public ResponseEntity<Object> unfollowNode(@PathVariable(required = true) String username,@PathVariable(required = true) long node) {

        User user = userService.findByUsername(username);
        FeedNode feedNode = feedNodeService.get(node);
        
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        } else {
            
            user.getFollowingNodes().remove(feedNode);
            userService.save(user);
            return ResponseEntity.ok(user.getFollowingNodes().stream().map(FeedNode::getJSON)
                    .map(kong.unirest.json.JSONObject::toMap).collect(Collectors.toList()));
        }

    }
    
    
    
    
    
    
    
    @ApiOperation(value = "Create new Feed Node", httpMethod = "GET", nickname = "create feed node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/following_users/{username}")
    public ResponseEntity<Object> getFollowingUsersByUsername(@PathVariable(required = true) String username) {

        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        } else {

            return ResponseEntity.ok(user.getFollowing().stream().map(User::toResponse)
                    .map(kong.unirest.json.JSONObject::toMap).collect(Collectors.toList()));
        }

    }

    @ApiOperation(value = "Create new Feed Node", httpMethod = "GET", nickname = "create feed node")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = FeedNode.class),
            @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/follow_node/")
    public ResponseEntity<Object> getFollowNode(@PathVariable(required = true) Map<String, String> data) {

        JSONObject json = new JSONObject(data);

        User user = userService.findByUsername(json.getString("username"));
        FeedNode node = feedNodeService.get(Long.parseLong(json.getString("id")));

        if (user == null || node == null) {
            return ResponseEntity.badRequest().body("User does not exists");
        } else {
            user.getFollowingNodes().add(node);
            userService.save(user);
            return ResponseEntity.ok("Now following " + node.getName());
        }

    }

    @PostMapping("/upload/pfp/{username}")
    public ResponseEntity<Object> uplaodPfp(@RequestParam("file") MultipartFile file,
            @PathVariable("username") String username)
            throws IOException {
        User user = userService.findByUsername(username);

        user.setPfp(ImageUtils.compressImage(file.getBytes()));
        user.setPfpType(file.getContentType());
        userService.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Image uploaded successfully: " +
                        file.getOriginalFilename());
    }

    @PostMapping("/upload/banner/{username}")
    public ResponseEntity<Object> uploadBanner(@RequestParam("file") MultipartFile file,
            @PathVariable("username") String username)
            throws IOException {
        User user = userService.findByUsername(username);

        user.setBanner(ImageUtils.compressImage(file.getBytes()));
        user.setBannerType(file.getContentType());
        userService.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Image uploaded successfully: " +
                        file.getOriginalFilename());
    }

    @GetMapping(path = { "/user/{username}/get_pfp" }, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImagePfp(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(user.getPfpType()))
                .body((ImageUtils.decompressImage(user.getPfp())));
    }

    @GetMapping(path = { "/user/{username}/get_banner" }, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageBanner(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(user.getBannerType()))
                .body((ImageUtils.decompressImage(user.getBanner())));
    }

}