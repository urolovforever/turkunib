package com.tiu.turk.news.admin.controller;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.admin.dto.category.NewsCategoryDto;
import com.tiu.turk.news.admin.dto.news.NewsCreateDto;
import com.tiu.turk.news.admin.dto.news.NewsTranslationUpdateDto;
import com.tiu.turk.news.admin.dto.news.NewsUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryMapper;
import com.tiu.turk.news.admin.mapper.NewsMapper;
import com.tiu.turk.news.admin.service.NewsCategoryService;
import com.tiu.turk.news.admin.service.NewsService;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/news"})
public class NewsController {
    private final NewsService newsService;
    private final NewsCategoryService newsCategoryService;
    private final NewsMapper newsMapper;
    private final NewsCategoryMapper newsCategoryMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/news/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page news = this.newsService.getAllNews(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.newsMapper.toDto(arg_0));
        model.addAttribute("page", news);
        model.addAttribute("q", q);
        return "admin/news/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model) {
        if (!model.containsAttribute("news")) {
            NewsCreateDto createDto = new NewsCreateDto();
            Map<String, NewsTranslationUpdateDto> translations = new LinkedHashMap<String, NewsTranslationUpdateDto>();
            for (TranslationLocale locale : TranslationLocale.values()) {
                NewsTranslationUpdateDto translation = new NewsTranslationUpdateDto();
                translation.setLocale(locale.name());
                translations.put(locale.name(), translation);
            }
            createDto.setTranslations(translations);
            model.addAttribute("news", createDto);
        }
        List<NewsCategoryDto> categories = this.newsCategoryService.getAllCategories().stream().map(arg_0 -> this.newsCategoryMapper.toDto(arg_0)).toList();
        model.addAttribute("categories", categories);
        return "admin/news/create";
    }

    @GetMapping(value={"/edit/{newsId}"})
    public String edit(@PathVariable(value="newsId") Long newsId, Model model) {
        try {
            Stream<NewsCategoryDto> categories = this.newsCategoryService.getAllCategories().stream().map(arg_0 -> this.newsCategoryMapper.toDto(arg_0));
            model.addAttribute("categories", categories);
            model.addAttribute("news", this.newsMapper.toDto(this.newsService.getNewsById(newsId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/news/edit";
    }

    @GetMapping(value={"/show/{newsId}"})
    public String show(@PathVariable(value="newsId") Long newsId, Model model) {
        try {
            model.addAttribute("news", this.newsMapper.toDto(this.newsService.getNewsById(newsId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/news/show";
    }

    @PostMapping(value={"/create"})
    public String createNews(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="news") NewsCreateDto createDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<NewsCategoryDto> categories = this.newsCategoryService.getAllCategories().stream().map(arg_0 -> this.newsCategoryMapper.toDto(arg_0)).toList();
            model.addAttribute("categories", categories);
            model.addAttribute("news", createDto);
            return "admin/news/create";
        }
        try {
            NewsEntity entity = this.newsService.saveNews(file, this.newsMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "News created successfully");
            return "redirect:/admin/news/edit/" + entity.getId();
        }
        catch (Exception e) {
            List<NewsCategoryDto> categories = this.newsCategoryService.getAllCategories().stream().map(arg_0 -> this.newsCategoryMapper.toDto(arg_0)).toList();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categories);
            model.addAttribute("news", createDto);
            return "admin/news/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateNews(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="news") NewsUpdateDto updateDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<NewsCategoryDto> categories = this.newsCategoryService.getAllCategories().stream().map(arg_0 -> this.newsCategoryMapper.toDto(arg_0)).toList();
            updateDto.setCategory(new NewsCategoryDto(updateDto.getCategoryId()));
            model.addAttribute("categories", categories);
            model.addAttribute("news", updateDto);
            return "admin/news/edit";
        }
        try {
            NewsEntity entity = this.newsService.updateNews(updateDto.getId(), file, this.newsMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "News updated successfully");
            return "redirect:/admin/news/show/" + entity.getId();
        }
        catch (Exception e) {
            List<NewsCategoryDto> categories = this.newsCategoryService.getAllCategories().stream().map(arg_0 -> this.newsCategoryMapper.toDto(arg_0)).toList();
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", categories);
            model.addAttribute("news", updateDto);
            return "admin/news/edit";
        }
    }

    @PostMapping(value={"/delete/{newsId}"})
    public String delete(@PathVariable(value="newsId") Long newsId, RedirectAttributes redirectAttributes) {
        try {
            this.newsService.deleteNews(newsId);
            redirectAttributes.addFlashAttribute("success", "News deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @GetMapping(value={"/translate/{newsId}"})
    public String translate(@PathVariable(value="newsId") Long newsId, RedirectAttributes redirectAttributes) {
        try {
            List executedTranslations = this.newsService.executeTranslationTasks(newsId);
            redirectAttributes.addFlashAttribute("success", ("Translation tasks initialized." + (String)(executedTranslations.isEmpty() ? "" : " Locales: " + String.join((CharSequence)", ", executedTranslations))));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/news/show/" + newsId;
    }

    @Generated
    public NewsController(NewsService newsService, NewsCategoryService newsCategoryService, NewsMapper newsMapper, NewsCategoryMapper newsCategoryMapper) {
        this.newsService = newsService;
        this.newsCategoryService = newsCategoryService;
        this.newsMapper = newsMapper;
        this.newsCategoryMapper = newsCategoryMapper;
    }
}

