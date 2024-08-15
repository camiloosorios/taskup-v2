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
            tokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
            tokenRepository.save(token);

            return token.getToken();
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Token validate(String userToken) {
        try {
            tokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage());
        }
        Token token = tokenRepository.findByToken(userToken)
                .orElseThrow(()-> new NotFoundException("Token does not exist or is expired"));
        delete(token);

        return token;
    }

    private void delete(Token token) {
        try {
            tokenRepository.delete(token);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private String generateToken() {
        return Long.toString(Math.round((Math.random() + 1) * 100000));
    }
}
