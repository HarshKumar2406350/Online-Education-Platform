package com.onlineEducationPlatform.assignmentAndQuiz.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.beans.factory.annotation.Value;

public class QuizIdGenerator extends CustomIdGenerator {
    
    public QuizIdGenerator() {
        super(9); // 9-digit quiz ID as per requirements
    }

    @Override
    protected boolean idExists(SharedSessionContractImplementor session, 
                             Object object, String generatedId) {
        String query = "SELECT COUNT(q) FROM Quiz q WHERE q.id = :id";
        Long count = session.createQuery(query, Long.class)
                          .setParameter("id", generatedId)
                          .uniqueResult();
        return count != null && count > 0;
    }
}