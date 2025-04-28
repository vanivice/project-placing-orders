package com.attestation.project.app.model.db.repository;

import com.attestation.project.app.model.db.entity.Executor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutorRepository extends JpaRepository<Executor, String> {
    Executor findByEmail(String email);
}
