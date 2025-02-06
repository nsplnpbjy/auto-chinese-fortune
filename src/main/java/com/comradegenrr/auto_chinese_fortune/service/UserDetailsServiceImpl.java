package com.comradegenrr.auto_chinese_fortune.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.comradegenrr.auto_chinese_fortune.config.UserNotAvilableException;
import com.comradegenrr.auto_chinese_fortune.config.UserNotExistException;
import com.comradegenrr.auto_chinese_fortune.model.User;
import com.comradegenrr.auto_chinese_fortune.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotExistException("User not found"));
        if (!user.isActive()) {
            throw new UserNotAvilableException(username + " is not active");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
    }
}