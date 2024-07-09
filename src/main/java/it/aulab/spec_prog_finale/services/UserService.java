package it.aulab.spec_prog_finale.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.spec_prog_finale.dtos.UserDto;
import it.aulab.spec_prog_finale.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response);
    User findUserByEmail(String email);
}