package com.example.lms.controller;

import com.example.lms.dto.ReviewDTO;
import com.example.lms.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(Authentication auth, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.addReview(auth, reviewDTO));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ReviewDTO>> getCourseReviews(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.getCourseReviews(courseId));
    }
}
