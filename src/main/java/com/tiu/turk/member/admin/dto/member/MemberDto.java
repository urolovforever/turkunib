package com.tiu.turk.member.admin.dto.member;

import com.tiu.turk.image.dto.ImageDto;
import com.tiu.turk.member.admin.dto.country.CountryDto;
import com.tiu.turk.user.admin.dto.UserDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Generated;

public class MemberDto {
    private Long id;
    private String name;
    private ImageDto image;
    private Integer founded;
    private String address;
    private String url;
    private Integer studentsNumber;
    private Integer facultiesNumber;
    private CountryDto country;
    private UserDto author;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String additionalInfo;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
    public Integer getFounded() {
        return this.founded;
    }

    @Generated
    public String getAddress() {
        return this.address;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public Integer getStudentsNumber() {
        return this.studentsNumber;
    }

    @Generated
    public Integer getFacultiesNumber() {
        return this.facultiesNumber;
    }

    @Generated
    public CountryDto getCountry() {
        return this.country;
    }

    @Generated
    public UserDto getAuthor() {
        return this.author;
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
    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
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
    public void setFounded(Integer founded) {
        this.founded = founded;
    }

    @Generated
    public void setAddress(String address) {
        this.address = address;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    @Generated
    public void setFacultiesNumber(Integer facultiesNumber) {
        this.facultiesNumber = facultiesNumber;
    }

    @Generated
    public void setCountry(CountryDto country) {
        this.country = country;
    }

    @Generated
    public void setAuthor(UserDto author) {
        this.author = author;
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
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

