package com.ismael.movies.repository;

import com.ismael.movies.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;
import java.util.List;


@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {

    @Query("SELECT n FROM Notifications n JOIN n.users u WHERE u.id = :userId")
    List<Notifications> findByUserId(UUID userId);
}
