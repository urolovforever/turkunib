package com.tiu.turk.banner.common.repository;

import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerTranslationRepository
extends JpaRepository<BannerTranslationEntity, Long> {
}

