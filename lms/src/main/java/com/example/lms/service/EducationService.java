package com.example.lms.service;

import com.example.lms.dto.EducationDTO;
import com.example.lms.entity.Education;
import com.example.lms.entity.Student;
import com.example.lms.repository.EducationRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;
    private final StudentRepository studentRepository;

    /* ================= CREATE ================= */
    public Education createEducation(EducationDTO dto) {

        Student student = getCurrentStudent();

        Education education = new Education();
        education.setType(dto.getType());
        education.setInstituteName(dto.getInstituteName());
        education.setDepartment(dto.getDepartment());
        education.setStartYear(dto.getStartYear());
        education.setEndYear(dto.getEndYear());
        education.setStudent(student);

        return educationRepository.save(education);
    }

    /* ================= READ ================= */
    public List<Education> getMyEducations() {
        Student student = getCurrentStudent();
        return educationRepository.findByStudentId(student.getId());
    }

    /* ================= UPDATE ================= */
    public Education updateEducation(Long educationId, EducationDTO dto) {

        Student student = getCurrentStudent();

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        // 🔐 Ownership check
        if (!education.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        education.setType(dto.getType());
        education.setInstituteName(dto.getInstituteName());
        education.setDepartment(dto.getDepartment());
        education.setStartYear(dto.getStartYear());
        education.setEndYear(dto.getEndYear());

        return educationRepository.save(education);
    }

    /* ================= DELETE ================= */
    public void deleteEducation(Long educationId) {

        Student student = getCurrentStudent();

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!education.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        educationRepository.deleteById(educationId);
    }

    /* ================= HELPER ================= */
    private Student getCurrentStudent() {
        String email =
                SecurityContextHolder.getContext().getAuthentication().getName();

        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
