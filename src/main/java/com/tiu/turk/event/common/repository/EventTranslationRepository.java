package com.tiu.turk.event.common.repository;

import com.tiu.turk.event.common.entity.EventTranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTranslationRepository
extends JpaRepository<EventTranslationEntity, Long> {
}

