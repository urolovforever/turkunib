package com.tiu.turk.translation.controller;

import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.translation.mapper.TranslationTaskMapper;
import com.tiu.turk.translation.service.TranslationTaskService;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/admin/translation-tasks"})
public class TranslationTaskController {
    private final TranslationTaskService translationTaskService;
    private final TranslationTaskMapper translationTaskMapper;
    private static final int PAGE_SIZE = 20;

    @GetMapping(value={"/show/{moduleName}"})
    public String showModuleTasks(@PathVariable(value="moduleName") String moduleName, String q, Model model, Pageable pageable) {
        try {
            AppModule module = AppModule.valueOf((String)moduleName.toUpperCase());
            Page tasks = this.translationTaskService.getTasksByModule(module, pageable.getPageNumber(), 20).map(arg_0 -> this.translationTaskMapper.toDto(arg_0));
            model.addAttribute("page", tasks);
            model.addAttribute("q", q);
            model.addAttribute("moduleName", moduleName);
            model.addAttribute("humanReadableModuleName", module.getHumanReadableName());
            return "admin/translationtask/module";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/news/index";
        }
    }

    @GetMapping(value={"/show/{moduleName}/{entityId}"})
    public String showEntityTasks(@PathVariable(value="moduleName") String moduleName, @PathVariable(value="entityId") Long entityId, String q, Model model, Pageable pageable) {
        try {
            AppModule module = AppModule.valueOf((String)moduleName.toUpperCase());
            Page tasks = this.translationTaskService.getAllByModuleAndEntityId(module, entityId, pageable.getPageNumber(), 20).map(arg_0 -> this.translationTaskMapper.toDto(arg_0));
            model.addAttribute("page", tasks);
            model.addAttribute("q", q);
            model.addAttribute("humanReadableModuleName", module.getHumanReadableName());
            model.addAttribute("entityMetaData", this.translationTaskService.getEntityMetaData(module, entityId));
            return "admin/translationtask/module-entity";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    @Generated
    public TranslationTaskController(TranslationTaskService translationTaskService, TranslationTaskMapper translationTaskMapper) {
        this.translationTaskService = translationTaskService;
        this.translationTaskMapper = translationTaskMapper;
    }
}

