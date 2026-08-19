package com.tiu.turk.keydate.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.keydate.common.repository.KeyDateRepository;
import com.tiu.turk.keydate.web.dto.KeyDateView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KeyDateWebService {
    private final KeyDateRepository repository;

    @Transactional(readOnly = true)
    public List<KeyDateView> getKeyDates(TranslationLocale locale) {
        return this.repository.findEnabledWithTranslations().stream()
                .map(k -> new KeyDateView(k, locale))
                .toList();
    }
}
