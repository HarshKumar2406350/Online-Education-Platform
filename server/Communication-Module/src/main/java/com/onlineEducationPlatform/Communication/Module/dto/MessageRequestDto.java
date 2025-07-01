package com.onlineEducationPlatform.Communication.Module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {
    
    @NotBlank(message = "User1 ID cannot be empty")
    private String user1_id;

    @Size(max = 255, message = "Message cannot exceed 255 characters")
    private String user1_message = "";

    @NotBlank(message = "User1 role cannot be empty")
    private String user1_role;

    @NotBlank(message = "User2 ID cannot be empty")
    private String user2_id;

    
    private String user2_message = "";

    @NotBlank(message = "User2 role cannot be empty")
    private String user2_role;
}