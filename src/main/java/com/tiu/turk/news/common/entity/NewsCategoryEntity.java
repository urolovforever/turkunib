package com.tiu.turk.news.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="news_categories")
public class NewsCategoryEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=true)
    @JoinColumn(name="image_id")
    private ImageEntity image;
    @OneToMany(mappedBy="category", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, NewsCategoryTranslationEntity> translations = new EnumMap(TranslationLocale.class);
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
    public ImageEntity getImage() {
        return this.image;
    }

    @Generated
    public Map<TranslationLocale, NewsCategoryTranslationEntity> getTranslations() {
        return this.translations;
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
    public void setImage(ImageEntity image) {
        this.image = image;
    }

    @Generated
    public void setTranslations(Map<TranslationLocale, NewsCategoryTranslationEntity> translations) {
        this.translations = translations;
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

