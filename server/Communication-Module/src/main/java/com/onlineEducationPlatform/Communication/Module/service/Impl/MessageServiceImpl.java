package com.onlineEducationPlatform.Communication.Module.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.onlineEducationPlatform.Communication.Module.dto.ApiResponse;
import com.onlineEducationPlatform.Communication.Module.dto.MessageRequestDto;
import com.onlineEducationPlatform.Communication.Module.dto.MessageResponseDto;
import com.onlineEducationPlatform.Communication.Module.entity.Message;
import com.onlineEducationPlatform.Communication.Module.exception.MessageException;
import com.onlineEducationPlatform.Communication.Module.repository.MessageRepository;
import com.onlineEducationPlatform.Communication.Module.service.MessageService;
import com.onlineEducationPlatform.Communication.Module.service.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ModelMapper modelMapper;

    
    @Autowired
    private UserServiceClient userServiceClient;

     private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public MessageResponseDto createNewMessage(MessageRequestDto messageRequestDto) {
        logger.info("Creating new message between users {} and {}", 
        messageRequestDto.getUser1_id(), messageRequestDto.getUser2_id());

       //TODO:use user1 id and user2 id to send a request to user module to verify that the user with given id exist
       //use feign client to make the request
       //if no throw no such user found exception

               // 2. Validate department code by calling department service
       
        try {
          //create the message entity from the messsage request dto using model mapper
          
          Message message = modelMapper.map(messageRequestDto, Message.class);
          Message savedMessage = messageRepository.save(message);

          MessageResponseDto responseDto = modelMapper.map(savedMessage, MessageResponseDto.class);
          responseDto.setTimestamp(LocalDateTime.now().toString());

          logger.info("Successfully created message with ID: {}", savedMessage.getMessageId());
          return responseDto;


        } catch (Exception e) {
            throw new MessageException("Failed to create message", "MESSAGE_CREATION_FAILED");
        }
    }

    @Override
    public MessageResponseDto updateMessageResponse(MessageRequestDto messageRequestDto,String messageId) {
        Message existingMessage = messageRepository.findMessageBetweenUsersById(
                messageRequestDto.getUser1_id(),
                messageRequestDto.getUser2_id(),
                messageId
        ).orElseThrow(() -> new MessageException("Message not found", "MESSAGE_NOT_FOUND"));

        // Update existing message
        existingMessage.setUser2_message(messageRequestDto.getUser2_message());
        Message updatedMessage = messageRepository.save(existingMessage);

        MessageResponseDto responseDto = modelMapper.map(updatedMessage, MessageResponseDto.class);
        responseDto.setTimestamp(LocalDateTime.now().toString());
        return responseDto;
    }

    @Override
    public List<MessageResponseDto> getAllMessagesBetweenUsers(String user1Id, String user2Id) {
        List<Message> messages = messageRepository.findAllMessagesBetweenUsers(user1Id, user2Id);
        return messages.stream()
                .map(message -> {
                    MessageResponseDto dto = modelMapper.map(message, MessageResponseDto.class);
                    dto.setTimestamp(LocalDateTime.now().toString());
                    return dto;
                })
                .collect(Collectors.toList());
    }


}