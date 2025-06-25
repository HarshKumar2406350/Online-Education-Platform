package com.onlineEducationPlatform.assignmentAndQuiz.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.beans.factory.annotation.Value;

public class AssignmentIdGenerator extends CustomIdGenerator {
    
    public AssignmentIdGenerator() {
        super(7); // 7-digit assignment ID as per requirements
    }

    @Override
    protected boolean idExists(SharedSessionContractImplementor session, 
                             Object object, String generatedId) {
        String query = "SELECT COUNT(a) FROM Assignment a WHERE a.id = :id";
        Long count = session.createQuery(query, Long.class)
                          .setParameter("id", generatedId)
                          .uniqueResult();
        return count != null && count > 0;
    }
}