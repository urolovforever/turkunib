package com.tiu.turk.webinar.web.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.webinar.common.entity.WebinarEntity;
import com.tiu.turk.webinar.common.entity.WebinarRegistrationEntity;
import com.tiu.turk.webinar.common.repository.WebinarRegistrationRepository;
import com.tiu.turk.webinar.common.repository.WebinarRepository;
import com.tiu.turk.webinar.web.dto.WebinarView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WebinarWebService {
    private final WebinarRepository webinarRepository;
    private final WebinarRegistrationRepository registrationRepository;

    @Transactional(readOnly=true)
    public WebinarView getWebinarView(TranslationLocale locale, Long id) {
        WebinarEntity w = this.webinarRepository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Webinar not found with ID: " + id));
        long count = this.registrationRepository.countByWebinar_Id(w.getId());
        boolean full = w.getCapacity() != null && count >= w.getCapacity();
        return WebinarView.of(w, locale, count, full);
    }

    @Transactional(readOnly=true)
    public List<WebinarView> getWebinars(TranslationLocale locale) {
        return this.webinarRepository.findEnabledWithTranslations().stream()
                .map(w -> {
                    long count = this.registrationRepository.countByWebinar_Id(w.getId());
                    boolean full = w.getCapacity() != null && count >= w.getCapacity();
                    return WebinarView.of(w, locale, count, full);
                })
                .toList();
    }

    @Transactional
    public void register(Long webinarId, String fullName, String email) {
        String cleanEmail = email == null ? "" : email.trim().toLowerCase();
        if (fullName == null || fullName.isBlank() || cleanEmail.isEmpty()) {
            throw new IllegalArgumentException("Name and email are required.");
        }
        WebinarEntity webinar = this.webinarRepository.findById(webinarId)
                .orElseThrow(() -> new IllegalArgumentException("Webinar not found."));
        if (Boolean.FALSE.equals(webinar.getEnabled())) {
            throw new IllegalArgumentException("Registration is closed for this webinar.");
        }
        if (this.registrationRepository.existsByWebinar_IdAndEmailIgnoreCase(webinarId, cleanEmail)) {
            throw new IllegalArgumentException("You are already registered for this webinar.");
        }
        long count = this.registrationRepository.countByWebinar_Id(webinarId);
        if (webinar.getCapacity() != null && count >= webinar.getCapacity()) {
            throw new IllegalArgumentException("This webinar is full.");
        }
        WebinarRegistrationEntity registration = new WebinarRegistrationEntity();
        registration.setWebinar(webinar);
        registration.setFullName(fullName.trim());
        registration.setEmail(cleanEmail);
        this.registrationRepository.save(registration);
    }
}
