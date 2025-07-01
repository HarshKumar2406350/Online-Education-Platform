package com.onlineEducationPlatform.Communication.Module.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.onlineEducationPlatform.Communication.Module.dto.ApiResponse;
import com.onlineEducationPlatform.Communication.Module.dto.MessageRequestDto;
import com.onlineEducationPlatform.Communication.Module.dto.MessageResponseDto;
import com.onlineEducationPlatform.Communication.Module.service.MessageService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/messages")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageResponseDto>> createMessage(
            @Valid @RequestBody MessageRequestDto messageRequestDto) {
        log.info("Received request to create message between users {} and {}", 
                messageRequestDto.getUser1_id(), messageRequestDto.getUser2_id());

        MessageResponseDto response = messageService.createNewMessage(messageRequestDto);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Message created successfully"));
    }

    @PutMapping("/reply/{messageId}")
    public ResponseEntity<ApiResponse<MessageResponseDto>> updateMessageWithReply(
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            @PathVariable String messageId) {
        log.info("Received request to update message {} between users {} and {}",
                messageId, messageRequestDto.getUser1_id(), messageRequestDto.getUser2_id());

        MessageResponseDto response = messageService.updateMessageResponse(messageRequestDto, messageId);
        return ResponseEntity.ok(ApiResponse.success(response, "Message updated successfully"));
    }

    @GetMapping("/between/{user1Id}/{user2Id}")
    public ResponseEntity<ApiResponse<List<MessageResponseDto>>> getMessagesBetweenUsers(
            @PathVariable String user1Id,
            @PathVariable String user2Id) {
        log.info("Fetching messages between users {} and {}", user1Id, user2Id);

        List<MessageResponseDto> messages = messageService.getAllMessagesBetweenUsers(user1Id, user2Id);
        return ResponseEntity.ok(ApiResponse.success(messages, "Messages retrieved successfully"));
    }
}