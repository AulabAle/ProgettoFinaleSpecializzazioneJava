package it.aulab.spec_prog_finale.services;

import it.aulab.spec_prog_finale.dtos.UserDto;
import it.aulab.spec_prog_finale.models.User;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
}