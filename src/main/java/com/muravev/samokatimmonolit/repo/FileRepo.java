package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepo extends JpaRepository<FileEntity, UUID> {
}
