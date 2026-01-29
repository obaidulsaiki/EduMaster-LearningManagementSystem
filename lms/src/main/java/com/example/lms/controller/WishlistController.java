package com.example.lms.controller;

import com.example.lms.dto.WishlistResponseDTO;
import com.example.lms.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/toggle/{courseId}")
    public ResponseEntity<Map<String, String>> toggleWishlist(Authentication auth, @PathVariable Long courseId) {
        String message = wishlistService.toggleWishlist(auth, courseId);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDTO>> getMyWishlist(Authentication auth) {
        return ResponseEntity.ok(wishlistService.getStudentWishlist(auth));
    }

    @GetMapping("/check/{courseId}")
    public ResponseEntity<Map<String, Boolean>> checkIfInWishlist(Authentication auth, @PathVariable Long courseId) {
        boolean exists = wishlistService.isCourseInWishlist(auth, courseId);
        return ResponseEntity.ok(Map.of("inWishlist", exists));
    }
}
