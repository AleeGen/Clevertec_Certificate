package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.service.impl.TagService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> get() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagResponse> post(@RequestBody TagRequest tag) {
        return new ResponseEntity<>(tagService.save(tag), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TagResponse> put(@RequestBody TagRequest tag) {
        return new ResponseEntity<>(tagService.update(tag), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}