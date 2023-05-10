package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface FileService {

    FileEntity uploadFile(MultipartFile file);

    FileEntity getInfo(UUID fileId);

    byte[] downloadFile(UUID fileId);
}
