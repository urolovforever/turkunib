package com.tiu.turk.event.admin.dto;

import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Generated;

public class EventUpdateDto {
    @NotNull(message="ID must not be blank")
    private @NotNull(message="ID must not be blank") Long id;
    @Size(max=255, message="Organizer length must not exceed 255 characters")
    private @Size(max=255, message="Organizer length must not exceed 255 characters") String organizer;
    @Size(max=50, message="Language length must not exceed 50 characters")
    private @Size(max=50, message="Language length must not exceed 50 characters") String language;
    @NotBlank(message="Format must not be blank")
    @Size(max=100, message="Format length must not exceed 100 characters")
    private @NotBlank(message="Format must not be blank") @Size(max=100, message="Format length must not exceed 100 characters") String format;
    @Size(max=512, message="Place name length must not exceed 512 characters")
    private @Size(max=512, message="Place name length must not exceed 512 characters") String placeName;
    @Size(max=1024, message="Address length must not exceed 1024 characters")
    private @Size(max=1024, message="Address length must not exceed 1024 characters") String address;
    @NotBlank(message="Start date must not be blank")
    private @NotBlank(message="Start date must not be blank") String startAt;
    @Valid
    private Map<String, EventTranslationUpdateDto> translations;
    private String endAt;
    private boolean enabled;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getOrganizer() {
        return this.organizer;
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
    public String getPlaceName() {
        return this.placeName;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getStartAt() {
        return this.startAt;
    }

    @Generated
    public Map<String, EventTranslationUpdateDto> getTranslations() {
        return this.translations;
    }

    @Generated
    public String getEndAt() {
        return this.endAt;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
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
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    @Generated
    public void setTranslations(Map<String, EventTranslationUpdateDto> translations) {
        this.translations = translations;
    }

    @Generated
    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Generated
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Generated
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}

