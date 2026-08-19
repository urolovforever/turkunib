package com.tiu.turk.news.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.common.entity.NewsEntity;
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
@Table(name="news_i18n")
public class NewsTranslationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="news_id", nullable=false)
    private NewsEntity news;
    @Column(name="locale", nullable=false)
    @Enumerated(value=EnumType.STRING)
    private TranslationLocale locale;
    @Column(name="title", nullable=false, length=512)
    private String title;
    @Column(name="slug", nullable=false, length=512)
    private String slug;
    @Column(name="content", columnDefinition="TEXT", nullable=false)
    private String content;
    @Column(name="description", columnDefinition="TEXT", length=1024)
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
    public NewsEntity getNews() {
        return this.news;
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
    public String getSlug() {
        return this.slug;
    }

    @Generated
    public String getContent() {
        return this.content;
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
    public void setNews(NewsEntity news) {
        this.news = news;
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
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
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

