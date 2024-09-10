package com.ismael.movies.services;

import com.ismael.movies.model.Users.User;
import com.ismael.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    public User findUserByLogin(String login){
        return userRepository.findByLogin(login);
    }
}
