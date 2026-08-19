package com.tiu.turk.authentication.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginAdminController {
    @GetMapping(value={"/admin/login"})
    public String showLoginPage() {
        return "admin/login/index";
    }
}

