package com.tiu.turk.leadership.admin.service;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.leadership.admin.dto.LeadershipMemberTranslationFields;
import com.tiu.turk.leadership.admin.dto.LeadershipMemberTranslationsForm;
import com.tiu.turk.leadership.common.entity.LeadershipMemberEntity;
import com.tiu.turk.leadership.common.entity.LeadershipMemberTranslationEntity;
import com.tiu.turk.leadership.common.repository.LeadershipMemberRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class LeadershipAdminService {
    private final LeadershipMemberRepository repository;
    private final ImageService imageService;
    private final TranslationInitializerService translationInitializerService;

    public List<String> executeTranslationTasks(Long id) throws Exception {
        LeadershipMemberEntity m = this.getById(id);
        String sourceText = Stream.of(m.getPosition(), m.getOrganization(), m.getBio())
                .filter(v -> v != null && !v.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);
        return this.translationInitializerService.initializeLeadershipMemberTranslationTasks(
                id, TranslationLocale.defaultTargetLocales(), sourceText);
    }

    public List<LeadershipMemberEntity> getAll() {
        return this.repository.findAllByOrderByDisplayOrderAscIdAsc();
    }

    public LeadershipMemberEntity getById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
    }

    @Transactional
    public void update(Long id, String fullName, String position, String organization, String bio,
                       Integer displayOrder, MultipartFile photo, Long authorId) throws IOException {
        LeadershipMemberEntity member = this.getById(id);
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        member.setFullName(fullName.trim());
        member.setPosition(position);
        member.setOrganization(organization);
        member.setBio(bio);
        member.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        if (photo != null && !photo.isEmpty()) {
            ImageEntity stored = this.imageService.store(photo, photo.getOriginalFilename(), "leadership", authorId);
            member.setPhoto(stored);
        }
        this.repository.save(member);
    }

    @Transactional
    public void create(String fullName, String position, String organization, String bio,
                       Integer displayOrder, MultipartFile photo, Long authorId) throws IOException {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        LeadershipMemberEntity member = new LeadershipMemberEntity();
        member.setFullName(fullName.trim());
        member.setPosition(position);
        member.setOrganization(organization);
        member.setBio(bio);
        member.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        member.setEnabled(true);
        member.setAuthor(new UserEntity(authorId));
        if (photo != null && !photo.isEmpty()) {
            ImageEntity stored = this.imageService.store(photo, photo.getOriginalFilename(), "leadership", authorId);
            member.setPhoto(stored);
        }
        this.repository.save(member);
    }

    /** Empty 7-locale form so the create page language tabs render. */
    public LeadershipMemberTranslationsForm emptyForm() {
        LeadershipMemberTranslationsForm form = new LeadershipMemberTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            form.getTranslations().put(locale.name(), new LeadershipMemberTranslationFields());
        }
        return form;
    }

    /** Creates a member with all languages at once: EN → base entity, other locales → leadership_member_i18n. */
    @Transactional
    public void createWithTranslations(LeadershipMemberTranslationsForm form, String fullName, Integer displayOrder,
                                       MultipartFile photo, Long authorId) throws IOException {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        LeadershipMemberEntity member = new LeadershipMemberEntity();
        member.setFullName(fullName.trim());
        LeadershipMemberTranslationFields en = form.getTranslations().get(TranslationLocale.EN.name());
        if (en != null) {
            member.setPosition(trimToNull(en.getPosition()));
            member.setOrganization(trimToNull(en.getOrganization()));
            member.setBio(trimToNull(en.getBio()));
        }
        member.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        member.setEnabled(true);
        member.setAuthor(new UserEntity(authorId));
        if (photo != null && !photo.isEmpty()) {
            ImageEntity stored = this.imageService.store(photo, photo.getOriginalFilename(), "leadership", authorId);
            member.setPhoto(stored);
        }
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            LeadershipMemberTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null || isAllBlank(f)) {
                continue;
            }
            LeadershipMemberTranslationEntity t = new LeadershipMemberTranslationEntity();
            t.setLeadershipMember(member);
            t.setLocale(locale);
            t.setPosition(trimToNull(f.getPosition()));
            t.setOrganization(trimToNull(f.getOrganization()));
            t.setBio(trimToNull(f.getBio()));
            member.getTranslations().put(locale, t);
        }
        this.repository.save(member);
    }

    private static boolean isAllBlank(LeadershipMemberTranslationFields f) {
        return Stream.of(f.getPosition(), f.getOrganization(), f.getBio())
                .allMatch(v -> v == null || v.isBlank());
    }

    /** Builds the per-locale form: EN tab from the base entity (read-only reference), other locales from leadership_member_i18n. */
    @Transactional(readOnly = true)
    public LeadershipMemberTranslationsForm getTranslationsForm(Long id) {
        LeadershipMemberEntity m = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
        LeadershipMemberTranslationsForm form = new LeadershipMemberTranslationsForm();
        for (TranslationLocale locale : TranslationLocale.values()) {
            LeadershipMemberTranslationFields f = new LeadershipMemberTranslationFields();
            if (locale == TranslationLocale.EN) {
                f.setPosition(m.getPosition());
                f.setOrganization(m.getOrganization());
                f.setBio(m.getBio());
            } else {
                LeadershipMemberTranslationEntity t = m.getTranslations().get(locale);
                if (t != null) {
                    f.setPosition(t.getPosition());
                    f.setOrganization(t.getOrganization());
                    f.setBio(t.getBio());
                }
            }
            form.getTranslations().put(locale.name(), f);
        }
        return form;
    }

    /** Upserts the leadership_member_i18n rows for every non-EN locale from the submitted form. EN stays on the base entity. */
    @Transactional
    public void saveTranslations(Long id, LeadershipMemberTranslationsForm form) {
        LeadershipMemberEntity m = this.repository.findByIdWithTranslations(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
        for (TranslationLocale locale : TranslationLocale.values()) {
            if (locale == TranslationLocale.EN) {
                continue;
            }
            LeadershipMemberTranslationFields f = form.getTranslations().get(locale.name());
            if (f == null) {
                continue;
            }
            LeadershipMemberTranslationEntity t = m.getTranslations().get(locale);
            if (t == null) {
                t = new LeadershipMemberTranslationEntity();
                t.setLeadershipMember(m);
                t.setLocale(locale);
                m.getTranslations().put(locale, t);
            }
            t.setPosition(trimToNull(f.getPosition()));
            t.setOrganization(trimToNull(f.getOrganization()));
            t.setBio(trimToNull(f.getBio()));
        }
        this.repository.save(m);
    }

    private static String trimToNull(String v) {
        if (v == null) {
            return null;
        }
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}
