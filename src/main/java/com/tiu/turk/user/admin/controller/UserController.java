package com.tiu.turk.user.admin.controller;

import com.tiu.turk.user.admin.dto.RoleDto;
import com.tiu.turk.user.admin.dto.UserCreateDto;
import com.tiu.turk.user.admin.dto.UserUpdateDto;
import com.tiu.turk.user.admin.mapper.RoleMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import com.tiu.turk.user.admin.service.RoleAdminService;
import com.tiu.turk.user.admin.service.UserAdminService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.util.List;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/user"})
public class UserController {
    private final UserAdminService userAdminService;
    private final RoleAdminService roleAdminService;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    @GetMapping(value={"/", "/index", "/index.html"})
    public String index(@RequestParam(required=false) String q, Model model, Pageable pageable) {
        Page users = this.userAdminService.getAllUsers(pageable.getPageNumber(), pageable.getPageSize()).map(arg_0 -> this.userMapper.toDto(arg_0));
        model.addAttribute("page", users);
        model.addAttribute("q", q);
        return "admin/user/index";
    }

    @GetMapping(value={"/create"})
    public String crete(Model model) {
        List<RoleDto> roles = this.roleAdminService.getAllRoles().stream().map(arg_0 -> this.roleMapper.toDto(arg_0)).toList();
        model.addAttribute("roles", roles);
        return "admin/user/create";
    }

    @GetMapping(value={"/edit/{userId}"})
    public String edit(@PathVariable(value="userId") Long userId, Model model) {
        try {
            UserEntity user = this.userAdminService.getUserById(userId);
            List<RoleDto> roles = this.roleAdminService.getAllRoles().stream().map(arg_0 -> this.roleMapper.toDto(arg_0)).toList();
            model.addAttribute("user", this.userMapper.toDto(user));
            model.addAttribute("roles", roles);
            return "admin/user/edit";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/user/index";
        }
    }

    @GetMapping(value={"/show/{userId}"})
    public String show(@PathVariable(value="userId") Long userId, Model model) {
        try {
            UserEntity user = this.userAdminService.getUserById(userId);
            model.addAttribute("user", this.userMapper.toDto(user));
            return "admin/user/show";
        }
        catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/user/index";
        }
    }

    @PostMapping(value={"/create"})
    public String createUser(@ModelAttribute UserCreateDto userCreateDto, RedirectAttributes redirectAttributes) {
        try {
            UserEntity userEntity = this.userMapper.toEntity(userCreateDto);
            this.userAdminService.saveUser(userEntity);
            redirectAttributes.addFlashAttribute("success", "User created successfully");
            return "redirect:/admin/user/index";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/user/create";
        }
    }

    @PostMapping(value={"/update"})
    public String updateUser(@ModelAttribute UserUpdateDto userUpdateDto, RedirectAttributes redirectAttributes) {
        try {
            UserEntity userEntity = this.userMapper.toEntity(userUpdateDto);
            this.userAdminService.updateUser(userUpdateDto.id(), userEntity);
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
            return "redirect:/admin/user/edit/" + userUpdateDto.id();
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/user/edit/" + userUpdateDto.id();
        }
    }

    @PostMapping(value={"/delete/{userId}"})
    public String deleteUser(@PathVariable(value="userId") Long userId, RedirectAttributes redirectAttributes) {
        try {
            this.userAdminService.deleteUserById(userId);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
            return "redirect:/admin/user/index";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/user/edit/" + userId;
        }
    }

    @Generated
    public UserController(UserAdminService userAdminService, RoleAdminService roleAdminService, RoleMapper roleMapper, UserMapper userMapper) {
        this.userAdminService = userAdminService;
        this.roleAdminService = roleAdminService;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }
}

