package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.request.filter.impl.UserFilter;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.service.impl.UserServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping()
    public ResponseEntity<List<UserResponse>> get(UserFilter filter, Pageable pageable) {
        log.info("Request userGetAll: {}", filter);
        List<UserResponse> response = userService.findAll(filter, pageable);
        log.info("Response userGetAll: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        log.info("Request userGetById: {}", id);
        UserResponse response = userService.findById(id);
        log.info("Response userGetById: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> post(@RequestBody @Valid UserRequest user) {
        log.info("Request userPost: {}", user);
        UserResponse response = userService.save(user);
        log.info("Response userPost: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponse> put(@RequestBody @Valid UserRequest user) {
        log.info("Request userPut: {}", user);
        UserResponse response = userService.update(user);
        log.info("Response userPut: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request userDelete: {}", id);
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}