package com.onlineEducationPlatform.Communication.Module.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public abstract class CustomIdGenerator implements IdentifierGenerator {
    
    @Autowired
    protected Environment environment;  // Using Spring's Environment
    
    private final SecureRandom random = new SecureRandom();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) 
        throws HibernateException {
        int length = getLength();
        StringBuilder sb = new StringBuilder(generateIdPrefix());
        
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }

    protected abstract String generateIdPrefix();
    protected abstract int getLength();
}