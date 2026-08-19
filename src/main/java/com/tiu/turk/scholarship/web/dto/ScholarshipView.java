package com.tiu.turk.scholarship.web.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.entity.ScholarshipTranslationEntity;
import java.time.LocalDate;
import lombok.Getter;

/**
 * Locale-aware read view of a scholarship. Translatable text fields resolve to the
 * requested locale's translation when present (and non-blank), otherwise fall back to
 * the base English text stored on the entity. Non-translatable fields (deadline,
 * document, id) pass through from the base entity.
 */
@Getter
public class ScholarshipView {
    private final Long id;
    private final String title;
    private final String provider;
    private final String description;
    private final String content;
    private final String coverage;
    private final String eligibility;
    private final String amount;
    private final LocalDate deadline;
    private final FileEntity document;
    private final String academicYear;
    private final String note;
    private final LocalDate applicationStart;
    private final LocalDate timeline1Start;
    private final LocalDate timeline1End;
    private final LocalDate timeline2Start;
    private final LocalDate timeline2End;
    private final LocalDate timeline3Start;
    private final LocalDate timeline3End;
    private final LocalDate timeline4Start;
    private final LocalDate timeline4End;
    private final LocalDate timeline5Date;

    public ScholarshipView(ScholarshipEntity s, TranslationLocale locale) {
        ScholarshipTranslationEntity t = (locale == null || locale == TranslationLocale.EN)
                ? null
                : s.getTranslations().get(locale);
        this.id = s.getId();
        this.title = pick(t == null ? null : t.getTitle(), s.getTitle());
        this.provider = pick(t == null ? null : t.getProvider(), s.getProvider());
        this.description = pick(t == null ? null : t.getDescription(), s.getDescription());
        this.content = pick(t == null ? null : t.getContent(), s.getContent());
        this.coverage = pick(t == null ? null : t.getCoverage(), s.getCoverage());
        this.eligibility = pick(t == null ? null : t.getEligibility(), s.getEligibility());
        this.amount = pick(t == null ? null : t.getAmount(), s.getAmount());
        this.deadline = s.getDeadline();
        this.document = s.getDocument();
        this.academicYear = s.getAcademicYear();
        this.note = pick(t == null ? null : t.getNote(), s.getNote());
        this.applicationStart = s.getApplicationStart();
        this.timeline1Start = s.getTimeline1Start();
        this.timeline1End = s.getTimeline1End();
        this.timeline2Start = s.getTimeline2Start();
        this.timeline2End = s.getTimeline2End();
        this.timeline3Start = s.getTimeline3Start();
        this.timeline3End = s.getTimeline3End();
        this.timeline4Start = s.getTimeline4Start();
        this.timeline4End = s.getTimeline4End();
        this.timeline5Date = s.getTimeline5Date();
    }

    /** True while applications are accepted: start reached (if set) and deadline not passed (deadline day inclusive). */
    public boolean isOpenNow() {
        LocalDate today = LocalDate.now();
        boolean started = this.applicationStart == null || !today.isBefore(this.applicationStart);
        boolean notEnded = this.deadline == null || !this.deadline.isBefore(today);
        return started && notEnded;
    }

    private static String pick(String translated, String fallback) {
        return (translated != null && !translated.isBlank()) ? translated : fallback;
    }
}
