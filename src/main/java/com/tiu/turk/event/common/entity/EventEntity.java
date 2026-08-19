package com.tiu.turk.event.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="event", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, EventTranslationEntity> translations = new EnumMap(TranslationLocale.class);
    @Column(name="organizer", length=255)
    private String organizer;
    @Column(name="start_at", nullable=false)
    private LocalDateTime startAt;
    @Column(name="end_at")
    private LocalDateTime endAt;
    @Column(name="language", length=50)
    private String language;
    @Column(name="format", nullable=false)
    private String format;
    @Column(name="latitude", precision=10, scale=6)
    private BigDecimal latitude;
    @Column(name="longitude", precision=10, scale=6)
    private BigDecimal longitude;
    @Column(name="place_name", length=512)
    private String placeName;
    @Column(name="address", length=1024)
    private String address;
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
    public Map<TranslationLocale, EventTranslationEntity> getTranslations() {
        return this.translations;
    }

    @Generated
    public String getOrganizer() {
        return this.organizer;
    }

    @Generated
    public LocalDateTime getStartAt() {
        return this.startAt;
    }

    @Generated
    public LocalDateTime getEndAt() {
        return this.endAt;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getFormat() {
        return this.format;
    }

    @Generated
    public BigDecimal getLatitude() {
        return this.latitude;
    }

    @Generated
    public BigDecimal getLongitude() {
        return this.longitude;
    }

    @Generated
    public String getPlaceName() {
        return this.placeName;
    }

    @Generated
    public String getAddress() {
        return this.address;
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
    public void setTranslations(Map<TranslationLocale, EventTranslationEntity> translations) {
        this.translations = translations;
    }

    @Generated
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @Generated
    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    @Generated
    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setFormat(String format) {
        this.format = format;
    }

    @Generated
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Generated
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Generated
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
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

