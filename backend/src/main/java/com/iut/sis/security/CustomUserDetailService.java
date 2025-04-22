package com.iut.sis.security;

import com.iut.sis.entity.User;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User ", " email : " + username, 0));
    }

}