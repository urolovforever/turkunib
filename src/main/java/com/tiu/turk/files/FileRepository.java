package com.tiu.turk.files;

import com.tiu.turk.files.FileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository
extends JpaRepository<FileEntity, Long> {
    public boolean existsByFilePathAndFileName(String var1, String var2);

    public boolean existsByMd5Hash(String var1);

    public FileEntity findByMd5Hash(String var1);

    public List<FileEntity> findByCreatedByModule(String var1);
}

