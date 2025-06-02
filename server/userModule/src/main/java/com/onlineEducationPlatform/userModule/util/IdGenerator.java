package com.onlineEducationPlatform.userModule.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.persistence.Table;
import java.util.Random;

@Component
public class IdGenerator implements IdentifierGenerator {
    
    @Value("${id.generator.length:6}")
    private int idLength;
    
    
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) 
            throws HibernateException {
        Random random = new Random();
        String generatedId;
        
        do {
            // Calculate min and max values based on idLength
            long minValue = (long) Math.pow(10, idLength - 1);
            long maxValue = (long) Math.pow(10, idLength) - 1;
            
            // Generate random number within range
            long randomNumber = minValue + ((long) (random.nextDouble() * (maxValue - minValue)));
            generatedId = String.valueOf(randomNumber);
            
            // Get table name using EntityPersister
            String tableName = object.getClass().getAnnotation(Table.class).name();
            if (tableName == null || tableName.isEmpty()) {
                tableName = object.getClass().getSimpleName().toLowerCase();
            }
            
            
            // Check if ID exists in the table
            String query = String.format("select count(*) from %s where id = %s", 
                                      tableName, generatedId);
            Long count = (Long) session.createNativeQuery(query, Long.class)
                                     .uniqueResult();
            
            if (count == 0) {
                return generatedId;
            }
        } while (true);
    }

    // Utility method to validate ID length
    private void validateIdLength(int length) {
        if (length < 1 || length > 19) { // 19 is max for Long
            throw new IllegalArgumentException(
                "ID length must be between 1 and 19 digits"
            );
        }
    }
}