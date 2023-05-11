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
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.request.filter.impl.TagFilter;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.service.impl.TagServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagServiceImpl tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> get(TagFilter filter, Pageable pageable) {
        log.info("Request tagGetAll: {}", filter);
        List<TagResponse> response = tagService.findAll(filter, pageable);
        log.info("Response tagGetAll: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> get(@PathVariable Long id) {
        log.info("Request tagGetById: {}", id);
        TagResponse response = tagService.findById(id);
        log.info("Response tagGetById: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/most-used-by-user")
    public ResponseEntity<TagResponse> get() {
        log.info("Request tagGetByMostUsedByUser");
        TagResponse response = tagService.findMostUsedByUser();
        log.info("Response tagGetByMostUsedByUser: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagResponse> post(@RequestBody @Valid TagRequest tag) {
        log.info("Request tagPost: {}", tag);
        TagResponse response = tagService.save(tag);
        log.info("Response tagPost: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TagResponse> put(@RequestBody @Valid TagRequest tag) {
        log.info("Request tagPut: {}", tag);
        TagResponse response = tagService.update(tag);
        log.info("Response tagPut: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request tagDelete: {}", id);
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}