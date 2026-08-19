package com.tiu.turk.event.common.repository;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.common.dto.EventSingleTranslationDto;
import com.tiu.turk.event.common.entity.EventEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository
extends JpaRepository<EventEntity, Long> {
    @Query(value="select new com.tiu.turk.event.common.dto.EventSingleTranslationDto(\n            e.id, t.locale, t.title, t.slug, t.description, e.organizer, e.startAt, e.endAt,\n            e.language, e.format, e.latitude, e.longitude, e.placeName, e.createdAt\n        )\nfrom EventEntity e\njoin e.translations t\nwhere e.enabled = true\nand t.locale = (case when exists (select 1 from EventTranslationEntity ex where ex.event = e and ex.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\norder by e.createdAt desc\n")
    public List<EventSingleTranslationDto> getActiveEvents(@Param("locale") TranslationLocale locale, Pageable pageable);

    @Query(value="select new com.tiu.turk.event.common.dto.EventSingleTranslationDto(\n            e.id, t.locale, t.title, t.slug, t.description, e.organizer, e.startAt, e.endAt,\n            e.language, e.format, e.latitude, e.longitude, e.placeName, e.createdAt\n        )\nfrom EventEntity e\njoin e.translations t\nwhere e.enabled = true\nand t.locale = (case when exists (select 1 from EventTranslationEntity ex where ex.event = e and ex.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\norder by e.createdAt desc\n")
    public Page<EventSingleTranslationDto> getActiveEventsPage(@Param("locale") TranslationLocale locale, Pageable pageable);

    @Query(value="select new com.tiu.turk.event.common.dto.EventSingleTranslationDto(\n            e.id, t.locale, t.title, t.slug, t.description, e.organizer, e.startAt, e.endAt,\n            e.language, e.format, e.latitude, e.longitude, e.placeName, e.createdAt\n)\nfrom EventEntity e\njoin e.translations t\nwhere e.enabled = true\nand e.id = :id\nand t.locale = (case when exists (select 1 from EventTranslationEntity ex where ex.event = e and ex.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\n")
    public Optional<EventSingleTranslationDto> findByIdWithTranslations(@Param("id") Long id, @Param("locale") TranslationLocale locale);

    @Query("select distinct e from EventEntity e join e.translations t where e.enabled = true and ("
            + "lower(t.title) like :q or lower(t.description) like :q) order by e.createdAt desc")
    List<EventEntity> search(@Param("q") String q);
}

