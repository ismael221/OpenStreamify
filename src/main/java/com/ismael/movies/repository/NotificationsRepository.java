package com.ismael.movies.repository;

import com.ismael.movies.model.Notifications;
import com.ismael.movies.model.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;
import java.util.List;


@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {

    //@Query("SELECT n FROM Notifications n JOIN n.users u WHERE u.id = :userId and n.visualized = false")
    @Query("SELECT n FROM Notifications n JOIN UserNotification un ON un.notification = n WHERE un.visualized = false AND un.user.id = :userId")
    List<Notifications> findByUserId(@Param("userId") UUID userId);
}
