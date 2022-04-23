package com.gabriel.tfg.controller;

import java.util.List;

import com.gabriel.tfg.entity.GenericEntity;
import com.gabriel.tfg.service.GenericService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public abstract class GenericController<T extends GenericEntity<T>> {

    private final GenericService<T> service;

    public GenericController(GenericService<T> service) {
        this.service = service;
    }

    /**
     * @return responseEntity {@link ResponseEntity}
     *
     * @throws MethodArgumentNotValidException {@link MethodArgumentNotValidException}
     */
    @ApiOperation(value = "Retrieve all ", httpMethod = "GET")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @GetMapping("")
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/{id}")
    public ResponseEntity<T> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @PutMapping("")
    public ResponseEntity<T> update(@RequestBody T updated) {
        return ResponseEntity.ok(service.update(updated));
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @PostMapping("")
    public ResponseEntity<T> save(@RequestBody T created) {
        return ResponseEntity.ok(service.save(created));
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Ok");
    }
}
