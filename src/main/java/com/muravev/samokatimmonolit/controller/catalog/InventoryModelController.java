package com.muravev.samokatimmonolit.controller.catalog;

import com.muravev.samokatimmonolit.entity.InventoryModelEntity;
import com.muravev.samokatimmonolit.mapper.InventoryModelMapper;
import com.muravev.samokatimmonolit.model.in.command.inventorymodel.InventoryModelChangeApprovingCommand;
import com.muravev.samokatimmonolit.model.in.command.inventorymodel.InventoryModelCreateCommand;
import com.muravev.samokatimmonolit.model.out.InventoryModelFullOut;
import com.muravev.samokatimmonolit.service.InventoryModelReader;
import com.muravev.samokatimmonolit.service.InventoryModelSaver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventorymodels")
@RequiredArgsConstructor
public class InventoryModelController {
    private final InventoryModelReader modelReader;
    private final InventoryModelSaver modelSaver;
    private final InventoryModelMapper modelMapper;


    @GetMapping(params = "keyword")
    public List<InventoryModelFullOut> findAllAvailable(@RequestParam String keyword) {
        return modelReader.findAll(keyword).stream()
                .map(modelMapper::toFullDto)
                .sorted(Comparator.comparing(InventoryModelFullOut::name))
                .toList();
    }

    @PostMapping
    @Secured({"ROLE_LOCAL_ADMIN","ROLE_GLOBAL_ADMIN"})
    public InventoryModelFullOut create(@RequestBody @Valid InventoryModelCreateCommand command) {
        InventoryModelEntity createdModel = modelSaver.create(command);
        return modelMapper.toFullDto(createdModel);
    }

    @PutMapping("/{id}/approve")
    @Secured({"ROLE_GLOBAL_ADMIN"})
    public InventoryModelFullOut approve(@PathVariable long id,
                                         @RequestBody @Valid InventoryModelChangeApprovingCommand command) {
        InventoryModelEntity updatedModel = modelSaver.updateApprove(id, command);
        return modelMapper.toFullDto(updatedModel);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        modelSaver.delete(id);
    }
}
