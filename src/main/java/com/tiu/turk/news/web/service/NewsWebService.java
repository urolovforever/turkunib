package com.tiu.turk.news.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.common.dto.NewsSingleTranslationDto;
import com.tiu.turk.news.common.repository.NewsRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NewsWebService {
    private final NewsRepository newsRepository;

    public List<NewsSingleTranslationDto> getActiveNews(TranslationLocale locale, int limit) {
        return this.newsRepository.findActiveNews(locale, (Pageable)PageRequest.of((int)0, (int)limit));
    }

    public Page<NewsSingleTranslationDto> getActiveNewsPaginated(TranslationLocale locale, int page, int size) {
        return this.newsRepository.findActiveNewsPage(locale, (Pageable)PageRequest.of((int)page, (int)size));
    }

    public NewsSingleTranslationDto getNewsById(TranslationLocale locale, Long newsId) {
        return (NewsSingleTranslationDto)this.newsRepository.findActiveNewsByIdWithTranslations(newsId, locale).orElseThrow(() -> new RuntimeException("News not found"));
    }

    @Generated
    public NewsWebService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }
}

