package com.tiu.turk.files;

import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileRepository;
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
public class FileService {
    @Value(value="${app.upload.dir}")
    private String uploadDir;
    private final FileRepository fileRepository;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    private static final String UNSUPPORTED_FILE_TYPE_MESSAGE = "Unsupported file type: %s. Allowed types are: %s";

    @Transactional
    public FileEntity store(MultipartFile file, String fileName, String createdByModule, Long authorId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String contentType = Optional.ofNullable(file.getContentType()).orElse("");
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(String.format("Unsupported file type: %s. Allowed types are: %s", file.getContentType(), ALLOWED_CONTENT_TYPES));
        }
        String ext = switch (contentType) {
            case "application/pdf" -> ".pdf";
            case "application/msword" -> ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> ".docx";
            case "application/vnd.ms-excel" -> ".xls";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> ".xlsx";
            default -> "";
        };
        String storedFileName = String.valueOf(UUID.randomUUID()) + ext;
        LocalDate now = LocalDate.now();
        String filePath = String.format("/files/%d%02d%02d/", now.getDayOfMonth(), now.getMonthValue(), now.getYear());
        Path dir = Paths.get(this.uploadDir + filePath, new String[0]).toAbsolutePath().normalize();
        if (!Files.exists(dir, new LinkOption[0])) {
            Files.createDirectories(dir, new FileAttribute[0]);
        }
        Path target = dir.resolve(storedFileName);
        file.transferTo(target);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setFilePath(filePath);
        fileEntity.setFileFileName(storedFileName);
        fileEntity.setFileContentType(contentType);
        fileEntity.setFileType(ext);
        fileEntity.setMd5Hash(FileService.md5Hash((byte[])file.getBytes()));
        fileEntity.setFileSize(Long.valueOf(file.getSize()));
        fileEntity.setCreatedByModule(createdByModule);
        fileEntity.setAuthor(new UserEntity(authorId));
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setUpdatedAt(LocalDateTime.now());
        return (FileEntity)this.fileRepository.save(fileEntity);
    }

    public void deletefile(Long id) throws IOException {
        Optional fileOpt = this.fileRepository.findById(id);
        if (!fileOpt.isPresent()) {
            throw new IllegalArgumentException("File with ID " + id + " not found");
        }
        FileEntity file = (FileEntity)fileOpt.get();
        Path filePath = Paths.get(this.uploadDir, file.getFilePath(), file.getFileFileName()).toAbsolutePath().normalize();
        Files.deleteIfExists(filePath);
        this.fileRepository.deleteById(id);
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
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
}

