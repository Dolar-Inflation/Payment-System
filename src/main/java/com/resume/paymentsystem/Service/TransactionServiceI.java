package com.resume.paymentsystem.Service;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.UUID;

public interface TransactionServiceI<T> {

    public Map<T,T> getMapOfEntityes(HttpSession session);
    public T findEntity(Object uniqId);


}
