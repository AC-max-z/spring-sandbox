package com.maxzamota.spring_sandbox.controllers;

import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AdviceController {
    @ExceptionHandler(CompromisedPasswordException.class)
    public String handleCompromisedPasswordException(CompromisedPasswordException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/reset-password";
    }
}
