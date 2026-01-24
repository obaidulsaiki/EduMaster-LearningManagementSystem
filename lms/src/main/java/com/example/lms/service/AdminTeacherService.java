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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    public Page<TeacherAdminDTO> getTeachers(int page, String search) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<Teacher> teachers =
                teacherRepository.searchTeachers(search, pageable);

        return teachers.map(this::toDto);
    }

    public TeacherAdminDTO getTeacher(Long id) {
        return toDto(
                teacherRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Teacher not found"))
        );
    }

    public TeacherAdminDTO updateTeacher(Long id, TeacherAdminDTO dto) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        TeacherProfile teacherProfile = teacherProfileRepository.findTeacherProfileByTeacher_Id(id);
        teacherProfile.setBio(dto.getBio());
        teacher.setName(dto.getName());
        teacher.setEnabled(dto.isEnabled());

        return toDto(teacherRepository.save(teacher));
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
        return dto;
    }
}
