package com.tiu.turk.member.common.entity;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="members")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", nullable=false, length=512)
    private String name;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="image_id")
    private ImageEntity image;
    @Column(name="founded")
    private Integer founded;
    @Column(name="address")
    private String address;
    @Column(name="url")
    private String url;
    @Column(name="students_number")
    private Integer studentsNumber;
    @Column(name="faculties_number")
    private Integer facultiesNumber;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="country_id")
    private CountryEntity country;
    @Column(name="latitude", precision=10, scale=6)
    private BigDecimal latitude;
    @Column(name="longitude", precision=10, scale=6)
    private BigDecimal longitude;
    @Column(name="additional_info", columnDefinition="TEXT")
    private String additionalInfo;
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
    public String getName() {
        return this.name;
    }

    @Generated
    public ImageEntity getImage() {
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
    public CountryEntity getCountry() {
        return this.country;
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
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setImage(ImageEntity image) {
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
    public void setCountry(CountryEntity country) {
        this.country = country;
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

