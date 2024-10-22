package com.sparta.iinewsfeedproject.repository;

import com.sparta.iinewsfeedproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Object> findByEmail(String email);
    Optional<User> findById(Long id);
}
