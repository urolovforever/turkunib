package com.tiu.turk.media.admin.dto;

import com.tiu.turk.user.admin.dto.UserDto;
import java.time.LocalDateTime;
import lombok.Generated;

public class MediaImageDto {
    private Long id;
    private String url;
    private String imageName;
    private String imageFileName;
    private String imageContentType;
    private Long imageSize;
    private UserDto author;
    private String createdByModule;
    private LocalDateTime createdAt;

    public String getHumanReadableSize() {
        if (this.imageSize < 1024L) {
            return this.imageSize + " B";
        }
        int unitIndex = (int)(Math.log(this.imageSize.longValue()) / Math.log(1024.0));
        String[] units = new String[]{"KB", "MB", "GB", "TB", "PB", "EB"};
        double convertedValue = (double)this.imageSize.longValue() / Math.pow(1024.0, unitIndex);
        return String.format("%.2f %s", convertedValue, units[unitIndex - 1]);
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUrl() {
        return this.url;
    }

    @Generated
    public String getImageName() {
        return this.imageName;
    }

    @Generated
    public String getImageFileName() {
        return this.imageFileName;
    }

    @Generated
    public String getImageContentType() {
        return this.imageContentType;
    }

    @Generated
    public Long getImageSize() {
        return this.imageSize;
    }

    @Generated
    public UserDto getAuthor() {
        return this.author;
    }

    @Generated
    public String getCreatedByModule() {
        return this.createdByModule;
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
    public void setUrl(String url) {
        this.url = url;
    }

    @Generated
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Generated
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Generated
    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Generated
    public void setImageSize(Long imageSize) {
        this.imageSize = imageSize;
    }

    @Generated
    public void setAuthor(UserDto author) {
        this.author = author;
    }

    @Generated
    public void setCreatedByModule(String createdByModule) {
        this.createdByModule = createdByModule;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

