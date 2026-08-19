package com.tiu.turk.media.admin.service;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.repository.ImageRepository;
import com.tiu.turk.image.service.ImageService;
import java.io.IOException;
import java.util.Optional;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MediaService {
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public Page<ImageEntity> getAllImage(int page, int size) {
        return this.imageRepository.findAll((Pageable)PageRequest.of((int)page, (int)size));
    }

    public void uploadImages(MultipartFile[] files, Long authorId) throws IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No files provided for upload");
        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
            this.imageService.store(file, fileName, "media", authorId);
        }
    }

    public void deleteImage(Long imageId) throws IOException {
        this.imageService.deleteImage(imageId);
    }

    @Generated
    public MediaService(ImageService imageService, ImageRepository imageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }
}

