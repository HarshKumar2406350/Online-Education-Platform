package com.onlineEducationPlatform.CourseManagement.util;

import org.springframework.beans.factory.annotation.Value;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

public class CourseIdGenerator extends CustomIdGenerator {
    
    public CourseIdGenerator() {
        super(5); // 5-digit course ID
    }

    @Override
    protected boolean idExists(SharedSessionContractImplementor session, 
                             Object object, String generatedId) {
        String query = "SELECT COUNT(c) FROM Course c WHERE c.id = :id";
        Long count = session.createQuery(query, Long.class)
                          .setParameter("id", generatedId)
                          .uniqueResult();
        return count != null && count > 0;
    }
}