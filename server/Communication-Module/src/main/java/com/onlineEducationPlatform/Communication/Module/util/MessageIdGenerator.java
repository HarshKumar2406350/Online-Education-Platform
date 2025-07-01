package com.onlineEducationPlatform.Communication.Module.util;

import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

@Component
public class MessageIdGenerator extends CustomIdGenerator {
    
    @Override
    public String generateIdPrefix() {
        return "MSG";
    }

    @Override
    public int getLength() {
        return environment.getProperty("id.generator.length.message", Integer.class, 9);
    }
}