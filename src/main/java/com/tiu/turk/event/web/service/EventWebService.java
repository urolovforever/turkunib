package com.tiu.turk.event.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.common.dto.EventSingleTranslationDto;
import com.tiu.turk.event.common.repository.EventRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventWebService {
    public final EventRepository eventRepository;

    public List<EventSingleTranslationDto> getActiveEvents(TranslationLocale locale, int limit) {
        return this.eventRepository.getActiveEvents(locale, Pageable.ofSize((int)limit));
    }

    public Page<EventSingleTranslationDto> getActiveEventsPaginated(TranslationLocale locale, int page, int size) {
        return this.eventRepository.getActiveEventsPage(locale, Pageable.ofSize((int)size).withPage(page));
    }

    public EventSingleTranslationDto getEventById(TranslationLocale locale, Long eventId) {
        return (EventSingleTranslationDto)this.eventRepository.findByIdWithTranslations(eventId, locale).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Generated
    public EventWebService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
}

