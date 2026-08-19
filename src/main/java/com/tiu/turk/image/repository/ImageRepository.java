package com.tiu.turk.image.repository;

import com.tiu.turk.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository
extends JpaRepository<ImageEntity, Long> {
    public boolean existsByImagePathAndImageName(String var1, String var2);

    public boolean existsByMd5Hash(String var1);

    public ImageEntity findByMd5Hash(String var1);
}

