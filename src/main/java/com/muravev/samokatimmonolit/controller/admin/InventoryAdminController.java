package com.muravev.samokatimmonolit.controller.admin;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.mapper.InventoryMapper;
import com.muravev.samokatimmonolit.model.out.InventoryCompactOut;
import com.muravev.samokatimmonolit.model.out.InventoryFullOut;
import com.muravev.samokatimmonolit.service.InventoryReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class InventoryAdminController {
    private final InventoryReader inventoryReader;
    private final InventoryMapper inventoryMapper;

    @GetMapping(value = "/orgs/{id}/inventories", params = {"page", "size", "keyword"})
    public Page<InventoryCompactOut> getAll(@PathVariable long id, @RequestParam("keyword") String keyword, Pageable pageable) {
        return inventoryReader.findAll(id, keyword, pageable)
                .map(inventoryMapper::toCompactDto);
    }

    @GetMapping("/inventories/{id}")
    public InventoryFullOut getOne(@PathVariable long id) {
        InventoryEntity inventory = inventoryReader.findById(id);
        return inventoryMapper.toFullDto(inventory);
    }

}
