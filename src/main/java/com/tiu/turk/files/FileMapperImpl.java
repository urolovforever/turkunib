package com.tiu.turk.files;

import com.tiu.turk.files.FileDto;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileMapper;
import org.springframework.stereotype.Component;

@Component
public class FileMapperImpl
implements FileMapper {
    public FileDto toDto(FileEntity entity) {
        if (entity == null) {
            return null;
        }
        FileDto fileDto = new FileDto();
        fileDto.setId(entity.getId());
        fileDto.setFileType(entity.getFileType());
        fileDto.setFileContentType(entity.getFileContentType());
        fileDto.setFileName(entity.getFileName());
        fileDto.setUpdatedAt(entity.getUpdatedAt());
        fileDto.setCreatedAt(entity.getCreatedAt());
        fileDto.setUrl(this.getFileUrl(entity));
        return fileDto;
    }
}

