package com.tiu.turk.news.admin.service;

import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface NewsCategoryService {
    public Page<NewsCategoryEntity> getAllCategories(int var1, int var2);

    public List<NewsCategoryEntity> getAllCategories();

    public NewsCategoryEntity getCategoryById(Long var1);

    public NewsCategoryEntity saveCategory(MultipartFile var1, NewsCategoryEntity var2, Long var3) throws IOException;

    public NewsCategoryEntity updateCategory(Long var1, MultipartFile var2, NewsCategoryEntity var3, Long var4) throws IOException;

    public void deleteCategory(Long var1);

    public List<String> executeTranslationTasks(Long var1) throws Exception;
}

