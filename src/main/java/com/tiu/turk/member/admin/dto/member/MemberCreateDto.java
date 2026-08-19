package com.tiu.turk.member.admin.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Generated;

public class MemberCreateDto {
    @NotBlank(message="Name must not be blank")
    @Size(max=512, message="Name length must not exceed 512 characters")
    private @NotBlank(message="Name must not be blank") @Size(max=512, message="Name length must not exceed 512 characters") String name;
    @Size(max=512, message="URL length must not exceed 512 characters")
    private @Size(max=512, message="URL length must not exceed 512 characters") String url;
    private Integer founded;
    private String address;
    private Integer studentsNumber;
    private Integer facultiesNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String additionalInfo;
    private Long countryId;
    private boolean enabled;

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getUrl() {
        return this.url;
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
    public Integer getStudentsNumber() {
        return this.studentsNumber;
    }

    @Generated
    public Integer getFacultiesNumber() {
        return this.facultiesNumber;
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
    public Long getCountryId() {
        return this.countryId;
    }

    @Generated
    public boolean isEnabled() {
        return this.enabled;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setUrl(String url) {
        this.url = url;
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
    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    @Generated
    public void setFacultiesNumber(Integer facultiesNumber) {
        this.facultiesNumber = facultiesNumber;
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
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    @Generated
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

