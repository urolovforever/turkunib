package com.tiu.turk.news.admin.controller;

import com.tiu.turk.news.admin.dto.category.NewsCategoryCreateDto;
import com.tiu.turk.news.admin.dto.category.NewsCategoryUpdateDto;
import com.tiu.turk.news.admin.mapper.NewsCategoryMapper;
import com.tiu.turk.news.admin.service.NewsCategoryService;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping(value={"/admin/news/category"})
public class NewsCategoryController {
    private final NewsCategoryService newsCategoryService;
    private final NewsCategoryMapper newsCategoryMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/news/category/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page categories = this.newsCategoryService.getAllCategories(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.newsCategoryMapper.toDto(arg_0));
        model.addAttribute("page", categories);
        model.addAttribute("q", q);
        return "admin/news/category/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model) {
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", new NewsCategoryCreateDto());
        }
        return "admin/news/category/create";
    }

    @GetMapping(value={"/edit/{categoryId}"})
    public String edit(Model model, @PathVariable Long categoryId) {
        try {
            model.addAttribute("category", this.newsCategoryMapper.toDto(this.newsCategoryService.getCategoryById(categoryId)));
            return "admin/news/category/edit";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
    }

    @GetMapping(value={"/show/{categoryId}"})
    public String show(Model model, @PathVariable Long categoryId) {
        try {
            model.addAttribute("category", this.newsCategoryMapper.toDto(this.newsCategoryService.getCategoryById(categoryId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/news/category/show";
    }

    @PostMapping(value={"/create"})
    public String createCategory(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="category") NewsCategoryCreateDto createDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", createDto);
            return "admin/news/category/create";
        }
        try {
            NewsCategoryEntity item = this.newsCategoryService.saveCategory(file, this.newsCategoryMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "News Category created successfully");
            return "redirect:/admin/news/category/edit/" + item.getId();
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("category", createDto);
            return "admin/news/category/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateCategory(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile file, @Valid @ModelAttribute(value="category") NewsCategoryUpdateDto updateDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", updateDto);
            return "admin/news/category/edit";
        }
        try {
            NewsCategoryEntity entity = this.newsCategoryService.updateCategory(updateDto.getId(), file, this.newsCategoryMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "News Category updated successfully");
            return "redirect:/admin/news/category/show/" + entity.getId();
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("category", updateDto);
            return "admin/news/category/edit";
        }
    }

    @PostMapping(value={"/delete/{categoryId}"})
    public String delete(@PathVariable(value="categoryId") Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            this.newsCategoryService.deleteCategory(categoryId);
            redirectAttributes.addFlashAttribute("success", "News Category deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @GetMapping(value={"/translate/{categoryId}"})
    public String translate(@PathVariable(value="categoryId") Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            List executedTranslations = this.newsCategoryService.executeTranslationTasks(categoryId);
            redirectAttributes.addFlashAttribute("success", ("Translation tasks initialized." + (String)(executedTranslations.isEmpty() ? "" : " Locales: " + String.join((CharSequence)", ", executedTranslations))));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/news/category/show/" + categoryId;
    }

    @Generated
    public NewsCategoryController(NewsCategoryService newsCategoryService, NewsCategoryMapper newsCategoryMapper) {
        this.newsCategoryService = newsCategoryService;
        this.newsCategoryMapper = newsCategoryMapper;
    }
}

