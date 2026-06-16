package com.resume.paymentsystem.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AccountServiceI<T> {

    public Map<T,T> getMapOfEntityes();
    public T findEntity(T uniqId);
    public ResponseEntity<T> createEntity(T dto);




}
