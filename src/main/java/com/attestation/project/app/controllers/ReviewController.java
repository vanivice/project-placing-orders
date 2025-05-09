package com.attestation.project.app.controllers;

import com.attestation.project.app.model.dto.request.ReviewInfoRequest;
import com.attestation.project.app.model.dto.response.ReviewInfoResponse;
import com.attestation.project.app.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "отзыв")

public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create/{id}")
    @Operation(summary = "Создать отзыв")
    public ReviewInfoResponse createReview(@RequestBody @Valid ReviewInfoRequest request, @PathVariable Long id) {
        return reviewService.createReview(request, id);
    }

    @GetMapping("/get-my-review")
    @Operation(summary = "Получить список оставленных отзывов")
    public List<ReviewInfoResponse> getMyReviews() {
        return reviewService.getMyReviews();
    }

    @GetMapping("/get-review/{id}")
    @Operation(summary = "Получить отзывы об исполнителе по его id")
    public List<ReviewInfoResponse> getReviewsByExecutor(@PathVariable Long id) {
        return reviewService.getReviewsByExecutor(id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Удалить отзыв")
    public ReviewInfoResponse deleteMyReview(@PathVariable Long id) {
        return reviewService.deleteMyReview(id);
    }
}
