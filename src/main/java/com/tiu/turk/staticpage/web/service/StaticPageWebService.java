package com.tiu.turk.staticpage.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.common.exception.PageNotFoundException;
import com.tiu.turk.staticpage.common.dto.StaticPageSingleTranslationDto;
import com.tiu.turk.staticpage.common.repository.StaticPageRepository;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class StaticPageWebService {
    private final StaticPageRepository staticPageRepository;

    public StaticPageSingleTranslationDto getStaticPageBySlug(String slug, TranslationLocale locale) {
        return (StaticPageSingleTranslationDto)this.staticPageRepository.findStaticPageBySlugAndLocale(slug, locale).orElseThrow(() -> new PageNotFoundException("Static page not found"));
    }

    @Generated
    public StaticPageWebService(StaticPageRepository staticPageRepository) {
        this.staticPageRepository = staticPageRepository;
    }
}

