package com.example.lms.service;

import com.example.lms.dto.StudentAdminDTO;
import com.example.lms.entity.Student;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminStudentService {

    private final StudentRepository studentRepository;

    public Page<StudentAdminDTO> getStudents(int page, String search) {
        String trimmedSearch = (search != null) ? search.trim() : null;
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        return studentRepository
                .searchStudents(trimmedSearch, pageable)
                .map(this::toDto);
    }

    public StudentAdminDTO getStudent(Long id) {
        return toDto(
                studentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Student not found")));
    }

    public void toggleStatus(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setEnabled(!student.isEnabled());
        studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    private StudentAdminDTO toDto(Student s) {
        StudentAdminDTO dto = new StudentAdminDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setEmail(s.getEmail());
        dto.setEnabled(s.isEnabled());
        dto.setCreatedAt(s.getCreatedAt());
        return dto;
    }
}
