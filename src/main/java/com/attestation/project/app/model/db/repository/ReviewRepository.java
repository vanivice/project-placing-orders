package com.attestation.project.app.model.db.repository;

import com.attestation.project.app.model.db.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByOrderId(String id);

    @Query(nativeQuery = true, value = "select * from reviews where estimator_id = :estimatorId")
    List<Review> getMyReview(@Param("estimatorId") Long id);

    @Query(nativeQuery = true, value = "select * from reviews where executor_id = :executorId")
    List<Review> getReviewByExecutorId(@Param("executorId") Long id);
}
