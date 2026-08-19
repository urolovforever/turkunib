package com.tiu.turk.leadership.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.leadership.common.repository.LeadershipMemberRepository;
import com.tiu.turk.leadership.web.dto.LeadershipMemberView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeadershipWebService {
    private final LeadershipMemberRepository repository;

    @Transactional(readOnly = true)
    public List<LeadershipMemberView> getMembers(TranslationLocale locale) {
        return this.repository.findEnabledWithTranslations().stream()
                .map(m -> new LeadershipMemberView(m, locale))
                .toList();
    }
}
