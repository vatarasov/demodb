package ru.vtarasov.demodb.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author vtarasov
 * @since 03.11.18
 */
@Controller
public class AdminController {

    @GetMapping("admin")
    public String get() {
        return "admin/admin";
    }
}
