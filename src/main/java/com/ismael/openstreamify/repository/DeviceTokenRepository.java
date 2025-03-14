package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken,UUID> {

    @Query("SELECT n FROM DeviceToken n WHERE n.userId = :userId")
    List<DeviceToken> findAllByUserId(@Param("userId") String id);


}
