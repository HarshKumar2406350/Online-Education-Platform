package com.onlineEducationPlatform.Communication.Module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    
    private String messageId;
    private String user1_id;
    private String user1_message;
    private String user1_role;
    private String user2_id;
    private String user2_message;
    private String user2_role;
    private String timestamp;
}