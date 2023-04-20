package ru.clevertec.taskspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.taskspring.entity.Tag;
import ru.clevertec.taskspring.service.impl.TagService;

import java.util.List;

@RestController
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<Tag>> get() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> get(@PathVariable int id) {
        System.out.println("id");
        return new ResponseEntity<>(tagService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/name/{partialName}")
    public ResponseEntity<List<Tag>> get(@PathVariable String partialName) {
        return new ResponseEntity<>(tagService.findByPartialName(partialName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tag> post(@RequestBody Tag tag) {
        tagService.save(tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Tag> put(@RequestBody Tag tag) {
        tagService.update(tag);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}