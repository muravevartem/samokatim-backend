package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.mapper.InventoryEventMappper;
import com.muravev.samokatimmonolit.mapper.InventoryMapper;
import com.muravev.samokatimmonolit.model.in.command.inventory.*;
import com.muravev.samokatimmonolit.model.out.InventoryEventOut;
import com.muravev.samokatimmonolit.model.out.InventoryFullOut;
import com.muravev.samokatimmonolit.service.InventoryReader;
import com.muravev.samokatimmonolit.service.InventorySaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventorySaver inventorySaver;
    private final InventoryReader inventoryReader;
    private final InventoryMapper inventoryMapper;
    private final InventoryEventMappper inventoryEventMappper;


    @GetMapping(params = {"page", "size", "my", "keyword"})
    @Secured("ROLE_LOCAL_ADMIN")
    public Page<InventoryFullOut> findAllAsEmployee(@RequestParam("keyword") String keyword, Pageable pageable) {
        return inventoryReader.findAllAsEmployee(keyword, pageable)
                .map(inventoryMapper::toFullDto);
    }

    @GetMapping(value = "/{id}",params = "my")
    @Secured("ROLE_LOCAL_ADMIN")
    public InventoryFullOut findByIdAsEmployee(@PathVariable long id) {
        InventoryEntity byIdAsEmployee = inventoryReader.findByIdAsEmployee(id);
        return inventoryMapper.toFullDto(byIdAsEmployee);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_CLIENT"})
    public InventoryFullOut findByIdAsClient(@PathVariable long id) {
        InventoryEntity byIdAsClient = inventoryReader.findByIdAsClient(id);
        return inventoryMapper.toFullDto(byIdAsClient);
    }

    @GetMapping("/{id}/events")
    @Secured("ROLE_LOCAL_ADMIN")
    public List<InventoryEventOut> findAllEventsById(@PathVariable long id) {
        return inventoryReader.findEventsById(id).stream()
                .map(inventoryEventMappper::toDto)
                .toList();
    }


    @PostMapping
    @Secured("ROLE_LOCAL_ADMIN")
    public InventoryFullOut create(@RequestBody @Valid InventoryCreateCommand command) {
        InventoryEntity createdInventory = inventorySaver.create(command);
        return inventoryMapper.toFullDto(createdInventory);
    }

    @PutMapping("/{id}/status")
    @Secured("ROLE_LOCAL_ADMIN")
    public InventoryFullOut updateStatus(@PathVariable long id,
                                         @RequestBody @Valid InventoryChangeStatusCommand command) {
        InventoryEntity updatedInventory = inventorySaver.changeField(id, command);
        return inventoryMapper.toFullDto(updatedInventory);
    }

    @PutMapping("/{id}/alias")
    @Secured("ROLE_LOCAL_ADMIN")
    public InventoryFullOut updateAlias(@PathVariable long id,
                                         @RequestBody @Valid InventoryChangeAliasCommand command) {
        InventoryEntity updatedInventory = inventorySaver.changeField(id, command);
        return inventoryMapper.toFullDto(updatedInventory);
    }

    @PutMapping("/{id}/model")
    @Secured("ROLE_LOCAL_ADMIN")
    public InventoryFullOut updateModel(@PathVariable long id,
                                        @RequestBody @Valid InventoryChangeModelCommand command) {
        InventoryEntity updatedInventory = inventorySaver.changeField(id, command);
        return inventoryMapper.toFullDto(updatedInventory);
    }

    @PutMapping("/{id}/class")
    @Secured("ROLE_LOCAL_ADMIN")
    public InventoryFullOut updateClass(@PathVariable long id,
                                        @RequestBody @Valid InventoryChangeClassCommand command) {
        InventoryEntity updatedInventory = inventorySaver.changeField(id, command);
        return inventoryMapper.toFullDto(updatedInventory);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_GLOBAL_ADMIN")
    public void delete(@PathVariable long id) {
        inventorySaver.delete(id);
    }
}
