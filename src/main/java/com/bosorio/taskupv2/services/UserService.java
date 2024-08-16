package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.UserDTO;

public interface UserService {

    void createUser(UserDTO userDTO);

    void confirmAccount(String token);

    String login(UserDTO userDTO);

    void sendConfirmationCode(UserDTO userDTO);

    void resetPassword(UserDTO userDTO);

    void validateToken(String token);

    void updatePassword(String token, UserDTO userDTO);
}
