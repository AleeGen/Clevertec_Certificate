package ru.clevertec.taskspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.taskspring.entity.GiftCertificate;
import ru.clevertec.taskspring.entity.Tag;
import ru.clevertec.taskspring.service.impl.GiftCertificateService;

import java.util.List;

@RestController
@RequestMapping(value = "/giftCert", produces = "application/json")
public class GiftCertificateController {

    private final GiftCertificateService certService;

    public GiftCertificateController(GiftCertificateService certService) {
        this.certService = certService;
    }

    @GetMapping
    public ResponseEntity<List<GiftCertificate>> get() {
        return new ResponseEntity<>(certService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> get(@PathVariable int id) {
        return new ResponseEntity<>(certService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/description/{partialDescription}")
    public ResponseEntity<List<GiftCertificate>> get(@PathVariable String partialDescription) {
        return new ResponseEntity<>(certService.findByPartialDescription(partialDescription), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> post(@RequestBody GiftCertificate giftCertificate) {
        certService.save(giftCertificate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GiftCertificate> put(@RequestBody GiftCertificate giftCertificate) {
        certService.update(giftCertificate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        certService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}