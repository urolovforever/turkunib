package com.tiu.turk.event.admin.controller;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.admin.dto.EventCreateDto;
import com.tiu.turk.event.admin.dto.EventTranslationUpdateDto;
import com.tiu.turk.event.admin.dto.EventUpdateDto;
import com.tiu.turk.event.admin.mapper.EventMapper;
import com.tiu.turk.event.admin.service.EventService;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.user.common.security.AppUserDetails;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/event"})
public class EventController {
    @Value(value="${app.yandex.api.key}")
    private String yandexApiKey;
    private final EventService eventService;
    private final EventMapper eventMapper;
    private static final String INDEX_URL_REDIRECT = "redirect:/admin/event/index";

    @GetMapping(value={"/", "/index", "/index.html"})
    public String getAllEvents(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page events = this.eventService.getAllEvents(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.eventMapper.toDto(arg_0));
        model.addAttribute("page", events);
        model.addAttribute("q", q);
        return "admin/event/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model) {
        if (!model.containsAttribute("event")) {
            EventCreateDto createDto = new EventCreateDto();
            Map<String, EventTranslationUpdateDto> translations = new LinkedHashMap<String, EventTranslationUpdateDto>();
            for (TranslationLocale locale : TranslationLocale.values()) {
                translations.put(locale.name(), new EventTranslationUpdateDto());
            }
            createDto.setTranslations(translations);
            model.addAttribute("event", createDto);
        }
        model.addAttribute("yandexApiKey", this.yandexApiKey);
        return "admin/event/create";
    }

    @GetMapping(value={"/edit/{eventId}"})
    public String edit(@PathVariable(value="eventId") Long eventId, Model model) {
        try {
            model.addAttribute("event", this.eventMapper.toDto(this.eventService.getEventById(eventId)));
            model.addAttribute("yandexApiKey", this.yandexApiKey);
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/event/edit";
    }

    @GetMapping(value={"/show/{eventId}"})
    public String show(@PathVariable(value="eventId") Long eventId, Model model) {
        try {
            model.addAttribute("event", this.eventMapper.toDto(this.eventService.getEventById(eventId)));
            model.addAttribute("yandexApiKey", this.yandexApiKey);
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return INDEX_URL_REDIRECT;
        }
        return "admin/event/show";
    }

    @PostMapping(value={"/create"})
    public String createEvent(@AuthenticationPrincipal AppUserDetails userDetails, @Valid @ModelAttribute(value="event") EventCreateDto createDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("event", createDto);
            model.addAttribute("yandexApiKey", this.yandexApiKey);
            return "admin/event/create";
        }
        try {
            EventEntity entity = this.eventService.saveEvent(this.eventMapper.toEntity(createDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Event created successfully");
            return "redirect:/admin/event/edit/" + entity.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/event/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateEvent(@AuthenticationPrincipal AppUserDetails userDetails, @Valid @ModelAttribute(value="event") EventUpdateDto updateDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("event", updateDto);
            model.addAttribute("yandexApiKey", this.yandexApiKey);
            return "admin/event/edit";
        }
        try {
            EventEntity entity = this.eventService.updateEvent(updateDto.getId(), this.eventMapper.toEntity(updateDto), userDetails.getId());
            redirectAttributes.addFlashAttribute("success", "Event updated successfully");
            return "redirect:/admin/event/show/" + entity.getId();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/event/edit/" + updateDto.getId();
        }
    }

    @PostMapping(value={"/delete/{eventId}"})
    public String deleteEvent(@PathVariable(value="eventId") Long eventId, RedirectAttributes redirectAttributes) {
        try {
            this.eventService.deleteEvent(eventId);
            redirectAttributes.addFlashAttribute("success", "Event deleted successfully");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return INDEX_URL_REDIRECT;
    }

    @GetMapping(value={"/translate/{eventId}"})
    public String translateEvent(@PathVariable(value="eventId") Long eventId, RedirectAttributes redirectAttributes) {
        try {
            List executedTranslations = this.eventService.executeTranslationTasks(eventId);
            redirectAttributes.addFlashAttribute("success", ("Translation tasks initialized." + (String)(executedTranslations.isEmpty() ? "" : " Locales: " + String.join((CharSequence)", ", executedTranslations))));
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", ("Error initializing translation: " + e.getMessage()));
        }
        return "redirect:/admin/event/show/" + eventId;
    }

    @Generated
    public EventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }
}

