package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.entites.User;

public interface TokenService {

    String create(User user);

    void validate(String token);

    void delete(String token);

}
