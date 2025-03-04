package com.ismael.openstreamify.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
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
    @Column(unique = true,nullable = false)
    private UUID rid ;
    private String message;
    private Date createdAt;
    @OneToMany(mappedBy = "notification")
    @ToString.Exclude
    private List<UserNotification> userNotifications;

}
