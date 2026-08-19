package com.tiu.turk.member.admin.dto.country;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import lombok.Generated;

public class CountryDto {
    private Long id;
    private String name;
    private ImageDto image;
    private UserDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CountryDto() {
    }

    public CountryDto(Long id) {
        this.id = id;
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public ImageDto getImage() {
        return this.image;
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
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setImage(ImageDto image) {
        this.image = image;
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

