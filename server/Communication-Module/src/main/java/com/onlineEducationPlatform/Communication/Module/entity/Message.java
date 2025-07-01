package com.onlineEducationPlatform.Communication.Module.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import com.onlineEducationPlatform.Communication.Module.util.MessageIdGenerator;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(generator = "message_id_generator")
    @GenericGenerator(
        name = "message_id_generator",
        type = MessageIdGenerator.class
    )
    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Column(name = "user1_id", nullable = false)
    private String user1_id;

    @Column(name = "user1_message", columnDefinition = "VARCHAR(255) DEFAULT ''", nullable = false)
    private String user1_message;

    @Column(name = "user1_role", nullable = false)
    private String user1_role;

    @Column(name = "user2_id", nullable = false)
    private String user2_id;

    @Column(name = "user2_message", columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String user2_message = ""; // Java

    @Column(name = "user2_role", nullable = false)
    private String user2_role;

}
