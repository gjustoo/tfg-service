package com.gabriel.tfg.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.service.UserService;
import com.gabriel.tfg.utils.PasswordUtils;

import org.springframework.beans.factory.annotation.Autowired;
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

@Api(value = "User controller", description = "Controller to manage users")
@RestController
@RequestMapping("users")
public class UserController extends GenericController<User> {

    @Autowired
    private UserService userService;

    public UserController(UserService service) {
        super(service);
    }

    @ApiOperation(value = "Creates user", httpMethod = "POST", nickname = "NEW_USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestParam(required = true) String name,
            @RequestParam(required = true) String email,
            @RequestParam(required = true) String passwd) {

        LocalDateTime creationTime = LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();

        String hashedpasswd = PasswordUtils.hash(passwd);

        User user = User.builder().name(name).email(email).passwd(hashedpasswd).creationDate(creationTime).build();

        if (userService.emailExists(email)) {
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            this.userService.save(user);
            return ResponseEntity.ok("ok");
        }

    }

}