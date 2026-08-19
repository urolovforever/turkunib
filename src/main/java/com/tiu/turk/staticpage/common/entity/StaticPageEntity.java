package com.tiu.turk.staticpage.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.common.StaticPageType;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="static_pages")
public class StaticPageEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="staticPage", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, StaticPageTranslationEntity> translations = new EnumMap(TranslationLocale.class);
    @Column(name="slug", nullable=false, unique=true, length=512)
    private String slug;
    @Column(name="enabled")
    private Boolean enabled = true;
    @Column(name="type")
    @Enumerated(value=EnumType.STRING)
    private StaticPageType type = StaticPageType.CUSTOM;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Map<TranslationLocale, StaticPageTranslationEntity> getTranslations() {
        return this.translations;
    }

    @Generated
    public String getSlug() {
        return this.slug;
    }

    @Generated
    public Boolean getEnabled() {
        return this.enabled;
    }

    @Generated
    public StaticPageType getType() {
        return this.type;
    }

    @Generated
    public UserEntity getAuthor() {
        return this.author;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTranslations(Map<TranslationLocale, StaticPageTranslationEntity> translations) {
        this.translations = translations;
    }

    @Generated
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Generated
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setType(StaticPageType type) {
        this.type = type;
    }

    @Generated
    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

