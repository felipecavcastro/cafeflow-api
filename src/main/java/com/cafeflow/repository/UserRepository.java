package com.cafeflow.repository;

import com.cafeflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método que usaremos no futuro para fazer o login
    Optional<User> findByUsername(String username);
}
