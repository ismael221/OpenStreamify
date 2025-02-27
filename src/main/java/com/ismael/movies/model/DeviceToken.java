package com.ismael.movies.model;

import com.ismael.movies.enums.DevicePlataform;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device_tokens")
public class    DeviceToken {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(unique = true,nullable = false)
    private UUID rid;
    private String deviceId;
    private String userId;
    private String fcmToken;
    @Enumerated(EnumType.STRING)
    private DevicePlataform platform;
    private Instant createdAt;
    private Instant updatedAt;

}
