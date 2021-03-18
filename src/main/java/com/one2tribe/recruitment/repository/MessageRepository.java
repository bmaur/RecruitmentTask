package com.one2tribe.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    @Query(value = "SELECT * FROM MESSAGES ORDER BY RANDOM() LIMIT :messageNumber", nativeQuery = true)
    List<MessageEntity> findRandomMessages(@Param("messageNumber") int messageNumber);
}
