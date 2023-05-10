package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.FileEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.repo.FileRepo;
import com.muravev.samokatimmonolit.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
public abstract class FileServiceImpl implements FileService {
    private static final String PATH_FORMAT = "/files/%s";

    private final FileRepo fileRepo;


    @Override
    @Transactional
    public FileEntity uploadFile(MultipartFile file) {
        UUID fileId = UUID.randomUUID();
        try {
            log.info("File uploading {}", fileId);
            String path = PATH_FORMAT.formatted(fileId);
            InputStream inputStream = file.getInputStream();
            boolean b = getFTPClient().storeFile(path, inputStream);
            if (!b)
                throw new ApiException(StatusCode.FILE_UPLOAD_WITH_ERROR);
        } catch (IOException e) {
            log.error("Error uploading file", e);
            throw new ApiException(StatusCode.FILE_UPLOAD_WITH_ERROR);
        }

        FileEntity fileEntity = new FileEntity()
                .setNew(true)
                .setId(fileId)
                .setExtension(file.getContentType())
                .setOriginalFilename(file.getOriginalFilename())
                .setBytes(file.getSize())
                .setLoadedAt(ZonedDateTime.now());
        return fileRepo.save(fileEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public FileEntity getInfo(UUID fileId) {
        return fileRepo.findById(fileId)
                .orElseThrow(() -> new ApiException(StatusCode.FILE_NOT_FOUND));
    }

    @Override
    public byte[] downloadFile(UUID fileId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            getFTPClient().retrieveFile(PATH_FORMAT.formatted(fileId), outputStream);
        } catch (IOException e) {
            throw new ApiException(StatusCode.FILE_UPLOAD_WITH_ERROR);
        }
        return outputStream.toByteArray();
    }

    protected abstract FTPClient getFTPClient();
}
