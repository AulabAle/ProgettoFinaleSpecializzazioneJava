package it.aulab.spec_prog_finale.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            model.addAttribute("error", "An error occurred");
            model.addAttribute("status", statusCode);
            model.addAttribute("url", request.getRequestURL());

            if (statusCode >= 400 && statusCode < 500) {
                return "error/4xx";
            } else if (statusCode >= 500 && statusCode < 600) {
                return "error/5xx";
            }
        }
        return "error/error";
    }
}
