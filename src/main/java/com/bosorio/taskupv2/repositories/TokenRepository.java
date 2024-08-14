package com.bosorio.taskupv2.repositories;

import com.bosorio.taskupv2.entites.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    void deleteAllByExpiresAtBefore(LocalDateTime expiresAt);
}
