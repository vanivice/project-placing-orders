package com.attestation.project.app.model.db.repository;

import com.attestation.project.app.model.db.entity.Customer;
import com.attestation.project.app.model.db.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderOrReceiver(Customer sender, Customer receiver);

    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver.id = :userId " +
            "UNION " +
            "SELECT DISTINCT m.receiver FROM Message m WHERE m.sender.id = :userId")
    List<Customer> findChatPartners(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
            "(m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetween(@Param("user1Id") Long user1Id,
                                              @Param("user2Id") Long user2Id);
}
