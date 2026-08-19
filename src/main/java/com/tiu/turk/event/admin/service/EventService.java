package com.tiu.turk.event.admin.service;

import com.github.slugify.Slugify;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.common.exception.PageNotFoundException;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.event.common.repository.EventRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final TranslationInitializerService initializerService;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public Page<EventEntity> getAllEvents(int page, int size) {
        return this.eventRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public EventEntity getEventById(Long eventId) {
        return (EventEntity)this.eventRepository.findById(eventId).orElseThrow(() -> new PageNotFoundException("Event not found with ID: " + eventId));
    }

    public EventEntity saveEvent(EventEntity event, Long authorId) {
        Map<TranslationLocale, EventTranslationEntity> incomingTranslations = event.getTranslations();
        EventTranslationEntity enTranslation = incomingTranslations != null ? (EventTranslationEntity)incomingTranslations.get(TranslationLocale.EN) : null;
        if (enTranslation == null || Optional.ofNullable(enTranslation.getTitle()).orElse("").isBlank()) {
            throw new IllegalArgumentException("English title must not be blank");
        }
        event.setAuthor(new UserEntity(authorId));
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        Map<TranslationLocale, EventTranslationEntity> translations = incomingTranslations != null ? Map.copyOf(incomingTranslations) : Map.of();
        if (event.getTranslations() != null) {
            event.getTranslations().clear();
        }
        EventEntity saved = (EventEntity)this.eventRepository.save(event);
        for (Map.Entry<TranslationLocale, EventTranslationEntity> entry : translations.entrySet()) {
            TranslationLocale locale = entry.getKey();
            EventTranslationEntity translation = entry.getValue();
            if (translation == null) {
                continue;
            }
            String safeTitle = Optional.ofNullable(translation.getTitle()).orElse("");
            if (safeTitle.isBlank()) {
                continue;
            }
            String safeDescription = Optional.ofNullable(translation.getDescription()).orElse("");
            translation.setLocale(locale);
            translation.setTitle(safeTitle);
            translation.setSlug(this.slugify.slugify(safeTitle));
            translation.setDescription(safeDescription);
            translation.setCreatedAt(LocalDateTime.now());
            translation.setUpdatedAt(LocalDateTime.now());
            translation.setEvent(saved);
            saved.getTranslations().put(locale, translation);
        }
        saved = (EventEntity)this.eventRepository.save(saved);
        return saved;
    }

    public EventEntity updateEvent(Long eventId, EventEntity updatedEvent, Long authorId) {
        EventEntity existingEvent = this.getEventById(eventId);
        for (Map.Entry entry : updatedEvent.getTranslations().entrySet()) {
            String safeTitle;
            TranslationLocale locale = (TranslationLocale)entry.getKey();
            EventTranslationEntity updatedTranslation = (EventTranslationEntity)entry.getValue();
            EventTranslationEntity existingTranslation = (EventTranslationEntity)existingEvent.getTranslations().get(locale);
            if (existingTranslation == null) {
                existingTranslation = new EventTranslationEntity();
                existingTranslation.setCreatedAt(LocalDateTime.now());
                existingTranslation.setEvent(existingEvent);
            }
            String baseForSlug = (safeTitle = Optional.ofNullable(updatedTranslation.getTitle()).orElse("")).isBlank() ? "event" : safeTitle;
            String safeDescription = Optional.ofNullable(updatedTranslation.getDescription()).orElse("");
            existingTranslation.setLocale(locale);
            existingTranslation.setTitle(safeTitle);
            existingTranslation.setSlug(this.slugify.slugify(baseForSlug));
            existingTranslation.setDescription(safeDescription);
            existingTranslation.setUpdatedAt(LocalDateTime.now());
            existingEvent.getTranslations().put(locale, existingTranslation);
        }
        existingEvent.setStartAt(updatedEvent.getStartAt());
        existingEvent.setEndAt(updatedEvent.getEndAt());
        existingEvent.setAddress(updatedEvent.getAddress());
        existingEvent.setPlaceName(updatedEvent.getPlaceName());
        existingEvent.setLatitude(updatedEvent.getLatitude());
        existingEvent.setLongitude(updatedEvent.getLongitude());
        existingEvent.setOrganizer(updatedEvent.getOrganizer());
        existingEvent.setFormat(updatedEvent.getFormat());
        existingEvent.setLanguage(updatedEvent.getLanguage());
        existingEvent.setEnabled(updatedEvent.getEnabled());
        existingEvent.setAuthor(new UserEntity(authorId));
        existingEvent.setUpdatedAt(LocalDateTime.now());
        return (EventEntity)this.eventRepository.save(existingEvent);
    }

    public void deleteEvent(Long eventId) {
        EventEntity existingEvent = this.getEventById(eventId);
        this.eventRepository.delete(existingEvent);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public List<String> executeTranslationTasks(Long eventId) throws Exception {
        EventEntity event = this.getEventById(eventId);
        EventTranslationEntity enLocale = (EventTranslationEntity)event.getTranslations().get(TranslationLocale.EN);
        return this.initializerService.initializeEventTranslationTasks(eventId, TranslationLocale.defaultTargetLocales(), enLocale.getTitle() + enLocale.getDescription());
    }

    @Generated
    public EventService(EventRepository eventRepository, TranslationInitializerService initializerService) {
        this.eventRepository = eventRepository;
        this.initializerService = initializerService;
    }
}

