package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.FileEntity;
import com.muravev.samokatimmonolit.model.out.FileOut;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper {
    FileOut toDto(FileEntity entity);
}
