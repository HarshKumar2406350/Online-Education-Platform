package com.onlineEducationPlatform.CourseManagement.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

public class EnrollmentIdGenerator extends CustomIdGenerator {
    
    public EnrollmentIdGenerator() {
        super(7); // 7-digit enrollment ID
    }

    @Override
    protected boolean idExists(SharedSessionContractImplementor session, 
                             Object object, String generatedId) {
        String query = "SELECT COUNT(e) FROM Enrollment e WHERE e.id = :id";
        Long count = session.createQuery(query, Long.class)
                          .setParameter("id", generatedId)
                          .uniqueResult();
        return count != null && count > 0;
    }
}