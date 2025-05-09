package com.attestation.project.app.service;

import com.attestation.project.app.exception.CommonBackendException;
import com.attestation.project.app.model.db.entity.Executor;
import com.attestation.project.app.model.db.entity.Review;
import com.attestation.project.app.model.db.repository.ExecutorRepository;
import com.attestation.project.app.model.db.repository.ReviewRepository;
import com.attestation.project.app.model.dto.request.ReviewInfoRequest;
import com.attestation.project.app.model.dto.response.ReviewInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ObjectMapper mapper;
    private final CustomerService customerService;
    private final ReviewRepository reviewRepository;
    private final ExecutorRepository executorRepository;
    private final OrderService orderService;

    public Review getReviewFromDB(Long id) {
        Optional<Review> optionalOrder = reviewRepository.findById(id);
        final String errMsg = ("Отзыв: " + id + " не найден");
        return optionalOrder.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    // создать отзыв
    public ReviewInfoResponse createReview(ReviewInfoRequest request, Long id) {

        orderService.getOrderFromDB(request.getOrderId());

        Executor executorWorkOrder = orderService.getOrderFromDB(request.getOrderId()).getCatalog().getExecutor();

        if (executorRepository.findById(id) == null) {
            throw new CommonBackendException("Ошибка: такого исполнителя нет", HttpStatus.CONFLICT);
        }

        if (executorWorkOrder == null || !executorWorkOrder.getId().equals(id)) {
            throw new CommonBackendException("Ошибка: на заказ не назначен исполнитель с id: " + id, HttpStatus.CONFLICT);
        }

        if (!orderService.getOrderFromDB(request.getOrderId()).getCustomer().getId()
                .equals(customerService.getCurrentUser().getId())) {
            throw new CommonBackendException("Ошибка: вы можете оставлять отзывы только по вашим заказам"
                    , HttpStatus.CONFLICT);
        }

        if (reviewRepository.findByOrderId(request.getOrderId()) != null) {
            throw new CommonBackendException("Ошибка: вы уже оставили отзыв по этому заказу", HttpStatus.CONFLICT);
        }

        Review review = mapper.convertValue(request, Review.class);

        review.setEstimator(customerService.getCurrentUser());
        review.setExecutor(executorRepository.findById(id));

        Review save = reviewRepository.save(review);

        return mapper.convertValue(save, ReviewInfoResponse.class);
    }

    // посмотреть свои отзывы
    public List<ReviewInfoResponse> getMyReviews() {

        Long currentId = customerService.getCurrentUser().getId();

        if (reviewRepository.getMyReview(currentId).isEmpty()) {
            throw new CommonBackendException("Ошибка: вы не оставили ни одного отзыва", HttpStatus.NOT_FOUND);
        }

        return reviewRepository.getMyReview(currentId).stream()
                .map(review -> mapper.convertValue(review, ReviewInfoResponse.class))
                .toList();
    }

    // посмотреть отзывы об исполнителе
    public List<ReviewInfoResponse> getReviewsByExecutor(Long id) {

        if (executorRepository.findById(id) == null) {
            throw new CommonBackendException("Ошибка: такого исполнителя нет", HttpStatus.CONFLICT);
        }

        if (reviewRepository.getReviewByExecutorId(id).isEmpty()) {
            throw new CommonBackendException("Ошибка: об этом исполнителе нет отзывов", HttpStatus.NOT_FOUND);
        }

        return reviewRepository.getReviewByExecutorId(id).stream()
                .map(review -> mapper.convertValue(review, ReviewInfoResponse.class))
                .toList();
    }

    // удалить отзыв
    public ReviewInfoResponse deleteMyReview(Long id) {

        Review review = getReviewFromDB(id);

        if (!review.getEstimator().getId().equals(customerService.getCurrentUser().getId())) {
            throw new CommonBackendException("Ошибка: вы не можете удалить этот отзыв", HttpStatus.CONFLICT);
        }

        reviewRepository.delete(review);

        ReviewInfoResponse message = new ReviewInfoResponse();
        message.setMessage("Отзыв: с id " + review.getId() + " удален");

        return  message;
    }
}
