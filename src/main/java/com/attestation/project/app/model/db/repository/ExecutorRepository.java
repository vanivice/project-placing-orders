package com.attestation.project.app.model.db.repository;

import com.attestation.project.app.model.db.entity.Executor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutorRepository extends JpaRepository<Executor, String> {
    Executor findByEmail(String email);

    Executor findById(Long id);

    @Query("select  u from Executor u where u.firstname like %:filter%")
    Page<Executor> findAllFiltered(Pageable pageRequest, @Param("filter") String filter);
}
