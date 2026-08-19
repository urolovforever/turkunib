package com.tiu.turk.staticpage.common.repository;

import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaticPageTranslationRepository
extends JpaRepository<StaticPageTranslationEntity, Long> {
}

