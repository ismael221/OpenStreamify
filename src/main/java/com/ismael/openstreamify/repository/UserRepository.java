package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByLogin(String login);

    @Query("select id from User")
    List<UUID> findAllUsersId();

    @Query("SELECT id FROM User u where u.login = :email")
    UUID findUserIdByLogin(String email);

}
