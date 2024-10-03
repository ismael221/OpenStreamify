package com.ismael.movies.model;

import com.ismael.movies.model.Users.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "rid")
@Entity
@Table(name = "notifications")
public class Notifications {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(unique = true,nullable = false)
    private UUID rid ;
    private String message;
    private Date createdAt;
    @OneToMany(mappedBy = "notification")
    private List<UserNotification> userNotifications;

}
