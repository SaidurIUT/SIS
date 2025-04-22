package com.iut.sis.service;

import com.iut.sis.entity.User;
import com.iut.sis.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

    @Autowired
    private UserRepo userRepo;

    public void createVerificationToken(User user , String token) {
        user.setVerificationToken(token);
        userRepo.save(user);
    }

    public String validateVerificationToken(String token) {
        User user = userRepo.findByVerificationToken(token).orElse(null);
        if(user == null) {
            return "Invalid verification token";
        }

        user.setEnabled(true);
        userRepo.save(user);
        return "Verification token validated";
    }

}