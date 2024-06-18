package it.aulab.spec_prog_finale.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.aulab.spec_prog_finale.dtos.UserDto;
import it.aulab.spec_prog_finale.models.Role;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.RoleRepository;
import it.aulab.spec_prog_finale.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));

        userRepository.save(user); 
    }
    
}