package com.example.lms.repository;

import com.example.lms.entity.Course;
import com.example.lms.entity.Student;
import com.example.lms.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByStudent(Student student);

    Optional<Wishlist> findByStudentAndCourse(Student student, Course course);

    boolean existsByStudentAndCourse(Student student, Course course);

    void deleteByStudentAndCourse(Student student, Course course);
}
