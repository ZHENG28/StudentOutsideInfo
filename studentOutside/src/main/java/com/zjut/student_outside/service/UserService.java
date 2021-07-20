package com.zjut.student_outside.service;

import com.zjut.student_outside.model.User;
import com.zjut.student_outside.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepository userRep;

    public Optional<User> login(User user)
    {
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole();
        return userRep.findByUsernameAndPasswordAndRole(username, password, role);
    }

    public Object update(User user)
    {
        return userRep.save(user);
    }
}
