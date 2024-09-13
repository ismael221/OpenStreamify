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

    public User updateUserLogin(User user){
        User user1 = findUserByLogin(user.getLogin());
        if (user1 != null){
            user1.setLogin(user.getLogin());
            user1.setRole(user.getRole());
            user1.setPassword(user.getPassword());
            userRepository.saveAndFlush(user1);
            return  userRepository.saveAndFlush(user1);
        }
        return null;
    }
}
