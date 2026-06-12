package com.resume.paymentsystem.DTO;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Builder
public record AccountDTO(String name, String password) implements Serializable {

    public AccountDTO{
        if (name.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Name and password are required");
        }
    }

//    private String name;
//    private String password;

}
