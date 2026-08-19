package com.tiu.turk.image.service;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.repository.ImageRepository;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    @Value(value="${app.upload.dir}")
    private String uploadDir;
    private final ImageRepository imageRepository;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp", "image/svg+xml");
    private static final String UNSUPPORTED_FILE_TYPE_MESSAGE = "Unsupported file type: %s. Allowed types are: %s";

    @Transactional
    public ImageEntity store(MultipartFile file, String imageName, String createdByModule, Long authorId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String contentType = Optional.ofNullable(file.getContentType()).orElse("");
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(String.format("Unsupported file type: %s. Allowed types are: %s", file.getContentType(), ALLOWED_CONTENT_TYPES));
        }
        String ext = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/bmp" -> ".bmp";
            case "image/webp" -> ".webp";
            case "image/svg+xml" -> ".svg";
            default -> "";
        };
        String storedFileName = String.valueOf(UUID.randomUUID()) + ext;
        LocalDate now = LocalDate.now();
        String imagePath = String.format("/images/%d%02d%02d/", now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        Path dir = Paths.get(this.uploadDir + imagePath, new String[0]).toAbsolutePath().normalize();
        if (!Files.exists(dir, new LinkOption[0])) {
            Files.createDirectories(dir, new FileAttribute[0]);
        }
        Path target = dir.resolve(storedFileName);
        file.transferTo(target);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageName(imageName);
        imageEntity.setImagePath(imagePath);
        imageEntity.setImageFileName(storedFileName);
        imageEntity.setImageContentType(contentType);
        imageEntity.setImageType(ext);
        imageEntity.setMd5Hash(ImageService.md5Hash((byte[])file.getBytes()));
        imageEntity.setImageSize(Long.valueOf(file.getSize()));
        imageEntity.setCreatedByModule(createdByModule);
        imageEntity.setAuthor(new UserEntity(authorId));
        imageEntity.setCreatedAt(LocalDateTime.now());
        imageEntity.setUpdatedAt(LocalDateTime.now());
        return (ImageEntity)this.imageRepository.save(imageEntity);
    }

    public void deleteImage(Long id) throws IOException {
        Optional imageOpt = this.imageRepository.findById(id);
        if (!imageOpt.isPresent()) {
            throw new IllegalArgumentException("Image with ID " + id + " not found");
        }
        ImageEntity image = (ImageEntity)imageOpt.get();
        Path imagePath = Paths.get(this.uploadDir, image.getImagePath(), image.getImageFileName()).toAbsolutePath().normalize();
        Files.deleteIfExists(imagePath);
        this.imageRepository.deleteById(id);
    }

    private static String md5Hash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Generated
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
}

