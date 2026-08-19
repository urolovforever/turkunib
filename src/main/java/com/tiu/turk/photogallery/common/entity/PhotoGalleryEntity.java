package com.tiu.turk.photogallery.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="photo_gallery")
public class PhotoGalleryEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="photoGallery", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, PhotoGalleryTranslationEntity> translations;
    @ManyToOne(fetch=FetchType.LAZY, optional=true)
    @JoinColumn(name="image_id")
    private ImageEntity previewImage;
    @ManyToMany
    @JoinTable(name="photo_gallery_image", joinColumns={@JoinColumn(name="gallery_id")}, inverseJoinColumns={@JoinColumn(name="image_id")})
    private List<ImageEntity> images = new ArrayList();
    @Column(name="enabled")
    private Boolean enabled = true;
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
    public Map<TranslationLocale, PhotoGalleryTranslationEntity> getTranslations() {
        return this.translations;
    }

    @Generated
    public ImageEntity getPreviewImage() {
        return this.previewImage;
    }

    @Generated
    public List<ImageEntity> getImages() {
        return this.images;
    }

    @Generated
    public Boolean getEnabled() {
        return this.enabled;
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
    public void setTranslations(Map<TranslationLocale, PhotoGalleryTranslationEntity> translations) {
        this.translations = translations;
    }

    @Generated
    public void setPreviewImage(ImageEntity previewImage) {
        this.previewImage = previewImage;
    }

    @Generated
    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }

    @Generated
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

