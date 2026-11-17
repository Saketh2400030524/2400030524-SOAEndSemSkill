package com.klu.inventory.controller;

import com.klu.inventory.model.InventoryItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final Map<Long, InventoryItem> items = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public InventoryController() {
        add(new InventoryItem(null, "Laptop", 10));
        add(new InventoryItem(null, "Keyboard", 25));
    }

    @GetMapping
    public Collection<InventoryItem> getAll() {
        return items.values();
    }

    @GetMapping("/{id}")
    public InventoryItem getById(@PathVariable Long id) {
        InventoryItem item = items.get(id);
        if (item == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found");
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItem create(@Valid @RequestBody InventoryItem item) {
        return add(item);
    }

    @PutMapping("/{id}")
    public InventoryItem update(@PathVariable Long id, @Valid @RequestBody InventoryItem item) {
        if (!items.containsKey(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found");
        InventoryItem updated = new InventoryItem(id, item.name(), item.quantity());
        items.put(id, updated);
        return updated;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (items.remove(id) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found");
    }

    private InventoryItem add(InventoryItem item) {
        long id = ids.incrementAndGet();
        InventoryItem saved = new InventoryItem(id, item.name(), item.quantity());
        items.put(id, saved);
        return saved;
    }

    public record InventoryItem(Long id, @NotBlank String name, @Min(0) int quantity) {}
}
