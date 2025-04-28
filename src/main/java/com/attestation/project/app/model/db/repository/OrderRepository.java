package com.attestation.project.app.model.db.repository;

import com.attestation.project.app.model.db.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = "select * from orders where catalog_id = :catalogId")
    Order getOrder(@Param("catalogId") Long id);

    @Query(nativeQuery = true, value = "select * from orders where customer_id = :customerId")
    List<Order> getMyOrder(@Param("customerId") Long id);

    Optional<Order> findById(String id);

    Order findByCatalogId(Long id);

    Optional<Order> findByTitleAndCost(String title, String Cost);
}
