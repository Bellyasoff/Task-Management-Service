package com.test.task.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class RefreshStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String token;
    private Date expiryDate;

    public RefreshStorage(String email, String token, Date expiryDate) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
