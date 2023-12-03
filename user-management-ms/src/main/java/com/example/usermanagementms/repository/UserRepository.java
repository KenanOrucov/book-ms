package com.example.usermanagementms.repository;

import com.example.usermanagementms.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserEntityByEmail(String email);

    @EntityGraph(attributePaths = {"authorities", "tokens"})
    Optional<User> findUserByEmail(String email);

    @EntityGraph(attributePaths = {"authorities", "tokens"} )
    List<User> findAll();

    User findUserByFirstName(String firstName);

    Optional<User> findUserByEmailAndPassword(String email, String password);
}
