package com.onlineEducationPlatform.Communication.Module.service;

import com.onlineEducationPlatform.Communication.Module.dto.MessageRequestDto;
import com.onlineEducationPlatform.Communication.Module.dto.MessageResponseDto;

import java.util.List;

public interface MessageService {
    MessageResponseDto createNewMessage(MessageRequestDto messageRequestDto);

    MessageResponseDto updateMessageResponse(MessageRequestDto messageRequestDto,String messageId);

    List<MessageResponseDto> getAllMessagesBetweenUsers(String user1Id, String user2Id);


} 