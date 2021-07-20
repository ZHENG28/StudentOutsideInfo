package com.zjut.student_outside.repository;

import com.zjut.student_outside.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByUsernameAndPasswordAndRole(String username, String password, String role);
}
