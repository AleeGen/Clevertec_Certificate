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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.ecl.dto.request.filter.impl.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.service.util.patch.PatchRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/gcs", produces = "application/json")
public class GiftCertificateController {

    private final GiftCertificateServiceImpl gcService;

    @GetMapping
    public ResponseEntity<List<GiftCertificateResponse>> get(GiftCertificateFilter filter, Pageable pageable) {
        log.info("Request gcGetAll: {}", filter);
        List<GiftCertificateResponse> response = gcService.findAll(filter, pageable);
        log.info("Response gcGetAll size: {}", response.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> get(@PathVariable Long id) {
        log.info("Request gcGetById: {}", id);
        GiftCertificateResponse response = gcService.findById(id);
        log.info("Response gcGetById: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateResponse> post(@RequestBody @Valid GiftCertificateRequest gc) {
        log.info("Request gcPost: {}", gc);
        GiftCertificateResponse response = gcService.save(gc);
        log.info("Response gcPost: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GiftCertificateResponse> put(@RequestBody @Valid GiftCertificateRequest gc) {
        log.info("Request gcPut: {}", gc);
        GiftCertificateResponse response = gcService.update(gc);
        log.info("Request gcPut: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> patch(@PathVariable Long id, @RequestBody @Valid PatchRequest gc) {
        log.info("Request gcPatch: {},{}", id, gc);
        GiftCertificateResponse response = gcService.patch(id, gc);
        log.info("Response gcPatch: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request gcDelete: {}", id);
        gcService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}