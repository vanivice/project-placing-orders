package com.attestation.project.app.model.db.repository;


import com.attestation.project.app.model.db.entity.OrderCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCatalogRepository extends JpaRepository<OrderCatalog, Long> {

    @Query("select  u from OrderCatalog u where u.description like %:filter%")
    Page<OrderCatalog> findAllFiltered(Pageable pageRequest, @Param("filter") String filter);

    @Query(nativeQuery = true, value = "select * from catalogs where executor_id = :executorId")
    List<OrderCatalog> getMyWorkOrder(@Param("executorId") Long id);
}
