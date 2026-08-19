package com.tiu.turk.event.admin.dto;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Generated;

public class EventCreateDto {
    @Valid
    private Map<String, EventTranslationUpdateDto> translations;
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
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String endAt;
    private TranslationLocale defaultLocale = TranslationLocale.EN;
    private boolean enabled;

    @Generated
    public Map<String, EventTranslationUpdateDto> getTranslations() {
        return this.translations;
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
    public BigDecimal getLatitude() {
        return this.latitude;
    }

    @Generated
    public BigDecimal getLongitude() {
        return this.longitude;
    }

    @Generated
    public String getEndAt() {
        return this.endAt;
    }

    @Generated
    public TranslationLocale getDefaultLocale() {
        return this.defaultLocale;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public void setTranslations(Map<String, EventTranslationUpdateDto> translations) {
        this.translations = translations;
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
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Generated
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Generated
    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    @Generated
    public void setDefaultLocale(TranslationLocale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

