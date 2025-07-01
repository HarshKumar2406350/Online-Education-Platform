package com.onlineEducationPlatform.Communication.Module.repository;

import com.onlineEducationPlatform.Communication.Module.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, String> {
    @Query("SELECT m FROM Message m WHERE m.user1_id = :user1Id AND m.user2_id = :user2Id")
    List<Message> findAllMessagesBetweenUsers(@Param("user1Id") String user1Id, @Param("user2Id") String user2Id);

    @Query("SELECT m FROM Message m WHERE m.user1_id = :user1Id AND m.user2_id = :user2Id AND m.messageId = :messageId")
    Optional<Message> findMessageBetweenUsersById(
            @Param("user1Id") String user1Id,
            @Param("user2Id") String user2Id,
            @Param("messageId") String messageId
    );

}
