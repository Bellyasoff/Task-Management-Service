package com.test.task.repository;

import com.test.task.model.RefreshStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshStorageRepository extends JpaRepository<RefreshStorage, Long> {

    Optional<RefreshStorage> findByToken(String token);
    String findByEmail(String email);
}
