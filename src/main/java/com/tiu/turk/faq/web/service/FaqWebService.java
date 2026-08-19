package com.tiu.turk.faq.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.faq.common.FaqCategory;
import com.tiu.turk.faq.common.repository.FaqRepository;
import com.tiu.turk.faq.web.dto.FaqView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaqWebService {
    private final FaqRepository repository;

    @Transactional(readOnly = true)
    public Map<FaqCategory, List<FaqView>> getFaqsGroupedByCategory(TranslationLocale locale) {
        return this.repository.findEnabledWithTranslations().stream()
                .map(f -> new FaqView(f, locale))
                .collect(Collectors.groupingBy(FaqView::getCategory, LinkedHashMap::new, Collectors.toList()));
    }
}
