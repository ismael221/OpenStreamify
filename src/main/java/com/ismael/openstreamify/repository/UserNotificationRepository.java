package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID> {
    @Modifying
    @Query("UPDATE UserNotification un SET un.visualized = true WHERE un.notification.id = :notificationId AND un.user.id = :userId")
    void markAsVisualized(@Param("notificationId") UUID notificationId, @Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE UserNotification un SET un.visualized = true WHERE un.user.id = :userId")
    void markAllAsVisualized(@Param("userId") UUID userId);
}
