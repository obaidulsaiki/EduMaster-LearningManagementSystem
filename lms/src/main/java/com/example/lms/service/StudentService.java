package com.example.lms.service;

import com.example.lms.dto.StudentResponseDTO;
import com.example.lms.entity.Student;
import com.example.lms.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    // 1. Get Student Profile
    public StudentResponseDTO getStudentProfile(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setSId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        return dto;
    }

    // 2. Update Student Profile
    public StudentResponseDTO updateStudentProfile(Long studentId, String newName, String newEmail) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        student.setName(newName);
        // Note: In production, updating email usually requires re-verification
        student.setEmail(newEmail);

        Student saved = studentRepository.save(student);

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setSId(saved.getId());
        dto.setName(saved.getName());
        dto.setEmail(saved.getEmail());
        return dto;
    }
}