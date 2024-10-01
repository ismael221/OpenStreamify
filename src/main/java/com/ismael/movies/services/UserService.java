package com.ismael.movies.services;

import com.ismael.movies.model.Exceptions.BadRequestException;
import com.ismael.movies.model.Users.RegisterDTO;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void createNewUser(RegisterDTO user){
        if (this.userRepository.findByLogin(user.login()) != null){
            throw new BadRequestException("User already registered, please login");
        };

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.login(),encryptedPassword,user.role());
        this.userRepository.save(newUser);

    }


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

    public List<UUID> findAllUsersId(){
        return  userRepository.findAllUsersId();
    }

}
