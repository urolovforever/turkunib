package com.tiu.turk.media.admin.controller;

import com.tiu.turk.media.admin.mapper.MediaMapper;
import com.tiu.turk.media.admin.service.MediaService;
import com.tiu.turk.user.common.security.AppUserDetails;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/media"})
public class MediaController {
    private final MediaService mediaService;
    private final MediaMapper mediaMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/media/images/index";

    @GetMapping(value={"/images/", "/images/index", "/images/index.html"})
    public String getAllImages(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page images = this.mediaService.getAllImage(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.mediaMapper.toMediaImageDto(arg_0));
        model.addAttribute("q", q);
        model.addAttribute("page", images);
        return "admin/media/images_index";
    }

    @PostMapping(value={"/images/upload"})
    public String uploadImages(@AuthenticationPrincipal AppUserDetails userDetails, @RequestParam(value="files") MultipartFile[] files, RedirectAttributes redirectAttributes) {
        try {
            this.mediaService.uploadImages(files, userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Images uploaded successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @PostMapping(value={"/images/delete/{imageId}"})
    public String deleteImage(@PathVariable(value="imageId") Long imageId, RedirectAttributes redirectAttributes) {
        try {
            this.mediaService.deleteImage(imageId);
            redirectAttributes.addFlashAttribute("success", "Image deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @Generated
    public MediaController(MediaService mediaService, MediaMapper mediaMapper) {
        this.mediaService = mediaService;
        this.mediaMapper = mediaMapper;
    }
}

