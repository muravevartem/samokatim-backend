package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.model.in.MapViewIn;
import com.muravev.samokatimmonolit.model.out.MapViewOut;
import com.muravev.samokatimmonolit.service.MapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/map-view")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;


    @PostMapping
    public MapViewOut getClientView(@Valid @RequestBody MapViewIn viewIn) {
        return mapService.getClientView(viewIn);
    }


    @PostMapping(params = {"my", "office"})
    public MapViewOut getMyOfficesView(@Valid @RequestBody MapViewIn viewIn) {
        return mapService.getMyOfficesView(viewIn);
    }

}
