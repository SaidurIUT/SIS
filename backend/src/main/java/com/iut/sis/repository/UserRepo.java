package com.iut.sis.repository;

import com.iut.sis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo  extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);

}
