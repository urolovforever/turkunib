package com.tiu.turk.files;

import org.mapstruct.Mapping;

import com.tiu.turk.files.FileDto;
import com.tiu.turk.files.FileEntity;

public interface FileMapper {
    @Mapping(target="url", expression="java(getFileUrl(entity))")
    public FileDto toDto(FileEntity var1);

    default public String getFileUrl(FileEntity entity) {
        if (entity == null) {
            return null;
        }
        return "/uploads" + entity.getFilePath() + entity.getFileFileName();
    }
}

