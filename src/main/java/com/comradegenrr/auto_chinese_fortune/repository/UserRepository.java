package com.comradegenrr.auto_chinese_fortune.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comradegenrr.auto_chinese_fortune.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}