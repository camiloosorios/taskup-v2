package com.bosorio.taskupv2.services.impl;

import com.bosorio.taskupv2.Exceptions.InternalServerErrorException;
import com.bosorio.taskupv2.Exceptions.NotFoundException;
import com.bosorio.taskupv2.entites.Token;
import com.bosorio.taskupv2.entites.User;
import com.bosorio.taskupv2.repositories.TokenRepository;
import com.bosorio.taskupv2.services.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public String create(User user) {
        Token token = Token.builder()
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .token(generateToken())
                .user(user)
                .build();
        try {
            tokenRepository.save(token);

            return token.getToken();
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void validate(String token) {
        try {
            tokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
        Token tokenExist = tokenRepository.findByToken(token)
                .orElseThrow(()-> new NotFoundException("Token does not exist or is expired"));
    }

    @Override
    public void delete(String token) {

    }

    private String generateToken() {
        return Long.toString(Math.round((Math.random() + 1) * 100000));
    }
}
