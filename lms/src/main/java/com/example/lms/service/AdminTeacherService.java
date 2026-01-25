package com.example.lms.service;

import com.example.lms.dto.TeacherAdminDTO;
import com.example.lms.entity.Teacher;
import com.example.lms.entity.TeacherProfile;
import com.example.lms.repository.TeacherProfileRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.lms.repository.TeacherEducationRepository;
import com.example.lms.repository.TeacherExperienceRepository;
import com.example.lms.dto.TeacherEducationDTO;
import com.example.lms.dto.TeacherExperienceDTO;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final TeacherEducationRepository teacherEducationRepository;
    private final TeacherExperienceRepository teacherExperienceRepository;

    public Page<TeacherAdminDTO> getTeachers(int page, String search) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<Teacher> teachers = teacherRepository.searchTeachers(search, pageable);

        return teachers.map(this::toDto);
    }

    public TeacherAdminDTO getTeacher(Long id) {
        return toDto(
                teacherRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Teacher not found")));
    }

    public TeacherAdminDTO updateTeacher(Long id, TeacherAdminDTO dto) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        TeacherProfile teacherProfile = teacherProfileRepository.findTeacherProfileByTeacher_Id(id);
        if (teacherProfile != null) {
            teacherProfile.setBio(dto.getBio());
            teacherProfileRepository.save(teacherProfile);
        }

        teacher.setName(dto.getName());
        teacher.setEnabled(dto.isEnabled());

        return toDto(teacherRepository.save(teacher));
    }

    public void toggleStatus(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        teacher.setEnabled(!teacher.isEnabled());
        teacherRepository.save(teacher);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

    private TeacherAdminDTO toDto(Teacher t) {
        TeacherAdminDTO dto = new TeacherAdminDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setEmail(t.getEmail());
        dto.setEnabled(t.isEnabled());

        TeacherProfile p = teacherProfileRepository.findTeacherProfileByTeacher_Id(t.getId());
        if (p != null) {
            dto.setBio(p.getBio());
        }

        dto.setEducations(teacherEducationRepository.findByTeacher_Id(t.getId()).stream().map(e -> {
            TeacherEducationDTO edto = new TeacherEducationDTO();
            edto.setId(e.getId());
            edto.setInstitution(e.getInstitution());
            edto.setDegree(e.getDegree());
            edto.setMajor(e.getMajor());
            edto.setStartDate(e.getStartDate());
            edto.setEndDate(e.getEndDate());
            return edto;
        }).collect(Collectors.toList()));

        dto.setExperiences(teacherExperienceRepository.findByTeacher_Id(t.getId()).stream().map(ex -> {
            TeacherExperienceDTO exdto = new TeacherExperienceDTO();
            exdto.setId(ex.getId());
            exdto.setCompany(ex.getCompany());
            exdto.setDesignation(ex.getDesignation());
            exdto.setStartDate(ex.getStartDate());
            exdto.setEndDate(ex.getEndDate());
            return exdto;
        }).collect(Collectors.toList()));

        return dto;
    }
}
