package com.klu.shipping.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {
    private final Map<Long, Shipment> shipments = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    @GetMapping
    public Collection<Shipment> getAll() {
        return shipments.values();
    }

    @GetMapping("/{id}")
    public Shipment getById(@PathVariable Long id) {
        Shipment shipment = shipments.get(id);
        if (shipment == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found");
        return shipment;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shipment create(@Valid @RequestBody CreateShipmentRequest request) {
        long id = ids.incrementAndGet();
        Shipment shipment = new Shipment(id, request.orderId(), request.destination(), "CREATED");
        shipments.put(id, shipment);
        return shipment;
    }

    @PatchMapping("/{id}/status")
    public Shipment updateStatus(@PathVariable Long id, @RequestParam @NotBlank String status) {
        Shipment current = shipments.get(id);
        if (current == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found");
        Shipment updated = new Shipment(id, current.orderId(), current.destination(), status.toUpperCase());
        shipments.put(id, updated);
        return updated;
    }

    public record CreateShipmentRequest(@NotBlank String orderId, @NotBlank String destination) {}
    public record Shipment(Long id, String orderId, String destination, String status) {}
}
