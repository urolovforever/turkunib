package com.tiu.turk.dashboard.admin.controller;

import com.tiu.turk.dashboard.admin.service.DashboardService;
import com.tiu.turk.user.common.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/admin"})
@RequiredArgsConstructor
public class DashBoardController {
    private final DashboardService dashboardService;

    @GetMapping(value={"/", "/dashboard", "/index", "/index.html"})
    public String index(@AuthenticationPrincipal AppUserDetails userDetails, Model model) {
        model.addAttribute("imageCount", this.dashboardService.countAllImages());
        model.addAttribute("totalImageSize", this.dashboardService.getAllImagesSize());
        model.addAttribute("newsCount", this.dashboardService.countAllNews());
        model.addAttribute("newsCategoryCount", this.dashboardService.countAllNewsCategories());
        model.addAttribute("photoGalleryCount", this.dashboardService.countAllPhotoGalleries());
        model.addAttribute("eventCount", this.dashboardService.countAllEvent());
        model.addAttribute("countryCount", this.dashboardService.countAllCountries());
        model.addAttribute("memberCount", this.dashboardService.countAllMembers());
        model.addAttribute("bannerCount", this.dashboardService.countAllBanners());
        model.addAttribute("staticPageCount", this.dashboardService.countAllStaticPages());
        model.addAttribute("userCount", this.dashboardService.countAllUsers());

        if (userDetails.isUniversityMember() && userDetails.getMemberName() != null) {
            model.addAttribute("recentApplications", this.dashboardService.getLatestApplicationByMemberName(userDetails.getMemberName()));
        } else {
            model.addAttribute("recentApplications", this.dashboardService.getLatestApplication(10L));
        }
        model.addAttribute("recentFeedbacks", this.dashboardService.getLatestFeedbacks(10L));
        return "admin/dashboard/index";
    }
}
