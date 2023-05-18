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
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.request.filter.impl.OrderFilter;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.service.impl.OrderServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> get(OrderFilter filter, Pageable pageable) {
        log.info("Request orderGetAll: {}", filter);
        List<OrderResponse> response = orderService.findAll(filter, pageable);
        log.info("Response orderGetAll: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        log.info("Request orderGetById: {}", id);
        OrderResponse response = orderService.findById(id);
        log.info("Response orderGetById: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> post(@RequestBody @Valid OrderRequest order) {
        log.info("Request orderPost: {}", order);
        OrderResponse response = orderService.save(order);
        log.info("Response orderPost: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OrderResponse> put(@RequestBody @Valid OrderRequest order) {
        log.info("Request orderPut: {}", order);
        OrderResponse response = orderService.update(order);
        log.info("Response orderPut: {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request orderDelete: {}", id);
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}