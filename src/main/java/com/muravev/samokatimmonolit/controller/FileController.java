package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.entity.FileEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.mapper.FileMapper;
import com.muravev.samokatimmonolit.model.out.FileOut;
import com.muravev.samokatimmonolit.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileMapper fileMapper;


    @PostMapping
    public FileOut upload(@RequestParam("file") MultipartFile file) {
        FileEntity fileEntity = fileService.uploadFile(file);
        return fileMapper.toDto(fileEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> loadFile(@PathVariable UUID id, HttpServletResponse response) {
        response.setStatus(200);
        byte[] file = fileService.downloadFile(id);
        FileEntity info = fileService.getInfo(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(info.getExtension()))
                .contentLength(file.length)
                .body(file);
    }
}
