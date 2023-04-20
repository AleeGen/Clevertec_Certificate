package ru.clevertec.ecl.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.service.impl.GiftCertificateService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/gcs", produces = "application/json")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService gcService;

    @GetMapping
    public ResponseEntity<List<GiftCertificateResponse>> get() {
        return new ResponseEntity<>(gcService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> get(@PathVariable Long id) {
        return new ResponseEntity<>(gcService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/byTagName/{name}")
    public ResponseEntity<List<GiftCertificateResponse>> getByTagName(
            @PathVariable String name,
            @RequestParam(required = false) @Pattern(regexp = "(asc)|(desc)") String sortType,
            String... sortBy) {
        return new ResponseEntity<>(gcService.findByTagName(name, sortType, sortBy), HttpStatus.OK);
    }

    @GetMapping("/byPart/{part}")
    public ResponseEntity<List<GiftCertificateResponse>> getByPart(
            @PathVariable String part,
            @RequestParam(required = false) @Pattern(regexp = "(asc)|(desc)") String sortType,
            String... sortBy) {
        return new ResponseEntity<>(gcService.findByPart(part, sortType, sortBy), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateResponse> post(@RequestBody GiftCertificateRequest gc) {
        return new ResponseEntity<>(gcService.save(gc), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GiftCertificateResponse> put(@RequestBody GiftCertificateRequest gc) {
        return new ResponseEntity<>(gcService.update(gc), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gcService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}