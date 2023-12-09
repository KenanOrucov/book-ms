package com.example.usermanagementms.repository;

import com.example.usermanagementms.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"authorities", "tokens"})
    Optional<User> findByUsername(String username);
}