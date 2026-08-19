package com.tiu.turk.event.admin.dto;

import com.tiu.turk.event.admin.dto.EventTranslationDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Generated;

public class EventDto {
    private Long id;
    private Map<String, EventTranslationDto> translations;
    private String startAt;
    private String endAt;
    private String address;
    private String placeName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String organizer;
    private String format;
    private String language;
    private boolean enabled;
    private UserDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Map<String, EventTranslationDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public String getStartAt() {
        return this.startAt;
    }

    @Generated
    public String getEndAt() {
        return this.endAt;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getPlaceName() {
        return this.placeName;
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
    public String getOrganizer() {
        return this.organizer;
    }

    @Generated
    public String getFormat() {
        return this.format;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public UserDto getAuthor() {
        return this.author;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTranslations(Map<String, EventTranslationDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    @Generated
    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    @Generated
    public void setFormat(String format) {
        this.format = format;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setAuthor(UserDto author) {
        this.author = author;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

