package com.tiu.turk.photogallery.admin.controller;

import com.tiu.turk.photogallery.admin.dto.PhotoGalleryCreateDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryTranslationUpdateDto;
import com.tiu.turk.photogallery.admin.dto.PhotoGalleryUpdateDto;
import com.tiu.turk.photogallery.admin.mapper.PhotoGalleryMapper;
import com.tiu.turk.photogallery.admin.service.PhotoGalleryService;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
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
@RequestMapping(value={"/admin/photo-gallery"})
public class PhotoGalleryController {
    private final PhotoGalleryService photoGalleryService;
    private final PhotoGalleryMapper photoGalleryMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/photo-gallery/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllGalleries(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page galleries = this.photoGalleryService.getAllGalleries(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.photoGalleryMapper.toDto(arg_0));
        model.addAttribute("page", galleries);
        model.addAttribute("q", q);
        return "admin/photogallery/index";
    }

    @GetMapping(value={"/create"})
    public String createGallery(Model model) {
        if (!model.containsAttribute("gallery")) {
            PhotoGalleryCreateDto createDto = new PhotoGalleryCreateDto();
            java.util.Map<String, PhotoGalleryTranslationUpdateDto> translations = new java.util.LinkedHashMap<String, PhotoGalleryTranslationUpdateDto>();
            for (com.tiu.turk.common.enums.TranslationLocale locale : com.tiu.turk.common.enums.TranslationLocale.values()) {
                translations.put(locale.name(), new PhotoGalleryTranslationUpdateDto());
            }
            createDto.setTranslations(translations);
            model.addAttribute("gallery", createDto);
        }
        return "admin/photogallery/create";
    }

    @GetMapping(value={"/edit/{galleryId}"})
    public String editGallery(@PathVariable(value="galleryId") Long galleryId, Model model) {
        try {
            model.addAttribute("gallery", this.photoGalleryMapper.toFullDto(this.photoGalleryService.getGalleryById(galleryId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/photogallery/edit";
    }

    @GetMapping(value={"/show/{galleryId}"})
    public String showGallery(@PathVariable(value="galleryId") Long galleryId, Model model) {
        try {
            model.addAttribute("gallery", this.photoGalleryMapper.toFullDto(this.photoGalleryService.getGalleryById(galleryId)));
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/photogallery/show";
    }

    @PostMapping(value={"/create"}, consumes={"multipart/form-data"})
    public String createPhotoGallery(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile previewImage, @RequestParam(value="files") MultipartFile[] imageFiles, @Valid @ModelAttribute(value="gallery") PhotoGalleryCreateDto createDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("gallery", createDto);
            return "admin/photogallery/create";
        }
        try {
            PhotoGalleryEntity entity = this.photoGalleryService.saveGallery(previewImage, imageFiles, this.photoGalleryMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Photo gallery created successfully.");
            return "redirect:/admin/photo-gallery/edit/" + entity.getId();
        }
        catch (Exception e) {
            model.addAttribute("error", ("Error creating photo gallery: " + e.getMessage()));
            model.addAttribute("gallery", createDto);
            return "admin/photogallery/create";
        }
    }

    @PostMapping(value={"/update"}, consumes={"multipart/form-data"})
    public String updatePhotoGallery(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="file") MultipartFile previewImage, @RequestParam(value="files") MultipartFile[] imageFiles, @ModelAttribute PhotoGalleryUpdateDto updateDto, RedirectAttributes redirectAttributes) {
        try {
            PhotoGalleryEntity entity = this.photoGalleryService.updateGallery(previewImage, imageFiles, this.photoGalleryMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Photo gallery updated successfully.");
            return "redirect:/admin/photo-gallery/show/" + entity.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", ("Error updating photo gallery: " + e.getMessage()));
            return "redirect:/admin/photo-gallery/edit/" + updateDto.getId();
        }
    }

    @PostMapping(value={"/delete/{galleryId}"})
    public String deleteGallery(@PathVariable(value="galleryId") Long galleryId, RedirectAttributes redirectAttributes) {
        try {
            this.photoGalleryService.deleteGallery(galleryId);
            redirectAttributes.addFlashAttribute("success", "Photo gallery deleted successfully.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", ("Error deleting photo gallery: " + e.getMessage()));
        }
        return INDEX_URL_REDIRECT;
    }

    @GetMapping(value={"/translate/{galleryId}"})
    public String translateGallery(@PathVariable(value="galleryId") Long galleryId, RedirectAttributes redirectAttributes) {
        try {
            List executedTranslations = this.photoGalleryService.executeTranslationTask(galleryId);
            redirectAttributes.addFlashAttribute("success", ("Translation tasks initialized." + (String)(executedTranslations.isEmpty() ? "" : " Locales: " + String.join((CharSequence)", ", executedTranslations))));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", ("Error creating translation tasks: " + e.getMessage()));
        }
        return "redirect:/admin/photo-gallery/show/" + galleryId;
    }

    @Generated
    public PhotoGalleryController(PhotoGalleryService photoGalleryService, PhotoGalleryMapper photoGalleryMapper) {
        this.photoGalleryService = photoGalleryService;
        this.photoGalleryMapper = photoGalleryMapper;
    }
}

