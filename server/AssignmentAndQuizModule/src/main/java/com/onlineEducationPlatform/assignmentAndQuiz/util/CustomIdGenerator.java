package com.onlineEducationPlatform.assignmentAndQuiz.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.util.Random;

public abstract class CustomIdGenerator implements IdentifierGenerator {
    
    private final int idLength;

    protected CustomIdGenerator(int idLength) {
        validateIdLength(idLength);
        this.idLength = idLength;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) 
            throws HibernateException {
        Random random = new Random();
        String generatedId;
        
        do {
            long minValue = (long) Math.pow(10, idLength - 1);
            long maxValue = (long) Math.pow(10, idLength) - 1;
            long randomNumber = minValue + ((long) (random.nextDouble() * (maxValue - minValue)));
            generatedId = String.valueOf(randomNumber);
            
            if (!idExists(session, object, generatedId)) {
                return generatedId;
            }
        } while (true);
    }

    protected abstract boolean idExists(SharedSessionContractImplementor session, 
                                     Object object, String generatedId);

    private void validateIdLength(int length) {
        if (length < 1 || length > 19) {
            throw new IllegalArgumentException("ID length must be between 1 and 19 digits");
        }
    }
}