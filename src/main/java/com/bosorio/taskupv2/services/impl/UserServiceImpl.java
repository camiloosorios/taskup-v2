package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.DTOs.UserDTO;
import com.bosorio.taskupv2.Exceptions.BadRequestException;
import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.User;
import com.bosorio.taskupv2.repositories.UserRepository;
import com.bosorio.taskupv2.services.helpers.EmailService;
import com.bosorio.taskupv2.services.TokenService;
import com.bosorio.taskupv2.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.emailService = emailService;
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
            emailService.sendConfirmationEmail(user.getEmail(), user.getName(), token);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Email already taken by another user");
        }catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void confirmAccount(String token) {
        Long userId = tokenService.validate(token).getId();
        User user = userRepository.findById(userId)
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
        if (userDTO.getPassword().trim().isEmpty()) {
            throw new BadRequestException("Password is required");
        }
        User user = getUserByEmail(userDTO);
        if(!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email or password are incorrect");
        }
    }

    @Override
    public void sendConfirmationCode(UserDTO userDTO) {
        User user = getUserByEmail(userDTO);
        if (!user.getConfirmed()) {
            throw new BadRequestException("User is already confirmed");
        }
        try {
            String token = tokenService.create(user);
            emailService.sendConfirmationEmail(user.getEmail(), user.getName(), token);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void resetPassword(UserDTO userDTO) {
        User user = getUserByEmail(userDTO);
        try {
            String token = tokenService.create(user);
            emailService.sendResetPasswordEmail(user.getEmail(), user.getName(), token);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void validateToken(String token) {
        tokenService.validate(token);
    }

    private User getUserByEmail(UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().trim().isEmpty()) {
            throw new BadRequestException("Email is required");
        }
        return userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
