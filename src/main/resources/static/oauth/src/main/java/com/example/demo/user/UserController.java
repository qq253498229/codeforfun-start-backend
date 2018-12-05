package com.example.demo.user;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangbin
 */
@RestController
public class UserController {

    @GetMapping("/registered")
    public ModelAndView registeredPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("registered.html");
    }

    @PostMapping("/registered")
    public ModelAndView registered(Model model, HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("login.html");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login.html");
    }
}
