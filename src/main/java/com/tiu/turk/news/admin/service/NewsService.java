package com.tiu.turk.news.admin.service;

import com.tiu.turk.news.common.entity.NewsEntity;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {
    public Page<NewsEntity> getAllNews(int var1, int var2);

    public NewsEntity getNewsById(Long var1);

    public NewsEntity saveNews(MultipartFile var1, NewsEntity var2, Long var3) throws IOException;

    public NewsEntity updateNews(Long var1, MultipartFile var2, NewsEntity var3, Long var4) throws IOException;

    public void deleteNews(Long var1);

    public List<String> executeTranslationTasks(Long var1) throws Exception;
}

