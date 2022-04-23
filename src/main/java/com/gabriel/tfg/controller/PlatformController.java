package com.gabriel.tfg.controller;

import com.gabriel.tfg.entity.Platform;
import com.gabriel.tfg.entity.User;
import com.gabriel.tfg.service.PlatformService;

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

@Api(value = "Platform controller", description = "Controller to manage platforms")
@RestController
@RequestMapping("platforms")
public class PlatformController extends GenericController<Platform> {

    @Autowired
    private PlatformService userService;

    public PlatformController(PlatformService service) {
        super(service);
    }

    @ApiOperation(value = "Create Platforms", httpMethod = "POST", nickname = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SUCCESS", response = User.class),
            @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/")
    public ResponseEntity<Object> createPlatform(
            @RequestParam(required = true) String name,
            @RequestParam(required = true) String url) {

        Platform user = Platform.builder().name(name).url(url).build();

        if (userService.platformExists(name)) {
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            this.userService.save(user);
            return ResponseEntity.ok("ok");
        }

    }

}