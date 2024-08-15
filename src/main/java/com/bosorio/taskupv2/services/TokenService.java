package com.bosorio.taskupv2.services;

import com.bosorio.taskupv2.entites.Token;
import com.bosorio.taskupv2.entites.User;

public interface TokenService {

    String create(User user);

    Token validate(String token);

}
