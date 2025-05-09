package com.attestation.project.app.model.db.repository;

import com.attestation.project.app.model.db.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
