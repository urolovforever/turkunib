package com.tiu.turk.banner.common.entity;

import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.common.enums.TranslationLocale;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="banners_i18n")
public class BannerTranslationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="banner_id", nullable=false)
    private BannerEntity banner;
    @Column(name="locale")
    @Enumerated(value=EnumType.STRING)
    private TranslationLocale locale;
    @Column(name="title", nullable=false, length=512)
    private String title;
    @Column(name="url_title", length=256)
    private String urlTitle;
    @Column(name="short_title", nullable=false, length=256)
    private String shortTitle;
    @Column(name="description", columnDefinition="TEXT")
    private String description;
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
    public BannerEntity getBanner() {
        return this.banner;
    }

    @Generated
    public TranslationLocale getLocale() {
        return this.locale;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getUrlTitle() {
        return this.urlTitle;
    }

    @Generated
    public String getShortTitle() {
        return this.shortTitle;
    }

    @Generated
    public String getDescription() {
        return this.description;
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
    public void setBanner(BannerEntity banner) {
        this.banner = banner;
    }

    @Generated
    public void setLocale(TranslationLocale locale) {
        this.locale = locale;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

    @Generated
    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
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

