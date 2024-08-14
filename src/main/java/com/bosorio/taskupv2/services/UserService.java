package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.DTOs.UserDTO;

public interface UserService {

    void createUser(UserDTO userDTO);

    void confirmAccount(Long id);

    void login(UserDTO userDTO);

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    void updateUser(UserDTO userDTO);

    void deleteUser(Long id);
}
