package com.tiu.turk.news.common.repository;

import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository
extends JpaRepository<NewsCategoryEntity, Long> {
}

