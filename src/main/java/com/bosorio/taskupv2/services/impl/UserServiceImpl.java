package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.User;
import com.bosorio.taskupv2.repositories.UserRepository;
import com.bosorio.taskupv2.services.TokenService;
import com.bosorio.taskupv2.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public void createUser(UserDTO userDTO) {
        if (userDTO.getPasswordConfirmation() == null ||
            !userDTO.getPassword().equals(userDTO.getPasswordConfirmation())) {
            throw new BadRequestException("Passwords do not match");
        }
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .build();

        try {
            userRepository.save(user);
            String token = tokenService.create(user);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void confirmAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getConfirmed()) {
            throw new BadRequestException("User is already confirmed");
        }
        user.setConfirmed(true);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void login(UserDTO userDTO) {
        if (userDTO.getEmail().trim().isEmpty() || userDTO.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Email and password are required");
        }
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new BadRequestException("Email or password are incorrect"));
        if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email or password are incorrect");
        }
    }

    @Override
    public UserDTO getUserById(Long id) {
        return null;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return null;
    }

    @Override
    public void updateUser(UserDTO userDTO) {

    }

    @Override
    public void deleteUser(Long id) {

    }
}
