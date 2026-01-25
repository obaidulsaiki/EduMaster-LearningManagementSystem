package com.example.lms.service;

import com.example.lms.dto.*;
import com.example.lms.entity.Teacher;
import com.example.lms.entity.TeacherEducation;
import com.example.lms.entity.TeacherExperience;
import com.example.lms.entity.TeacherProfile;
import com.example.lms.repository.TeacherEducationRepository;
import com.example.lms.repository.TeacherExperienceRepository;
import com.example.lms.repository.TeacherProfileRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final TeacherEducationRepository educationRepository;
    private final TeacherExperienceRepository experienceRepository;
    private final com.example.lms.repository.CourseRepository courseRepository;

    /* ================= HELPER ================= */

    private Teacher getTeacher(Authentication auth) {
        return teacherRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    /* ================= PROFILE ================= */

    public TeacherProfileResponseDTO getMyProfile(Authentication auth) {

        Teacher teacher = getTeacher(auth);

        TeacherProfile profile = (TeacherProfile) teacherProfileRepository
                .findTeacherProfileByTeacher_Id(teacher.getId());

        TeacherProfileResponseDTO dto = new TeacherProfileResponseDTO();
        dto.setTId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setEmail(teacher.getEmail());

        if (profile != null) {
            dto.setBio(profile.getBio());
            dto.setField(profile.getField());
            dto.setExperienceYears(profile.getExperienceYears());
            dto.setLinkedInUrl(profile.getLinkedInUrl());
            dto.setWebsite(profile.getWebsite());

            if (profile.getUniversities() != null) {
                dto.setUniversities(List.of(profile.getUniversities().split(",")));
            }
        }

        return dto;
    }

    public TeacherProfileResponseDTO saveOrUpdateProfile(
            Authentication auth,
            TeacherProfileRequestDTO request) {
        Teacher teacher = getTeacher(auth);
        if (teacherProfileRepository.findTeacherProfileByTeacher_Id(teacher.getId()) == null) {
            TeacherProfile p = new TeacherProfile();
            p.setTeacher(teacher);
            teacherProfileRepository.save(p);
        }

        TeacherProfile profile = (TeacherProfile) teacherProfileRepository
                .findTeacherProfileByTeacher_Id(teacher.getId());

        profile.setBio(request.getBio());
        profile.setField(request.getField());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setLinkedInUrl(request.getLinkedInUrl());
        profile.setWebsite(request.getWebsite());

        if (request.getUniversities() != null) {
            profile.setUniversities(String.join(",", request.getUniversities()));
        }

        teacherProfileRepository.save(profile);
        return getMyProfile(auth);
    }

    /* ================= EDUCATION ================= */

    public List<TeacherEducationDTO> getMyEducations(Authentication auth) {

        Teacher teacher = getTeacher(auth);
        List<TeacherEducation> list = educationRepository.findByTeacher_Id(teacher.getId());

        List<TeacherEducationDTO> result = new ArrayList<>();

        for (TeacherEducation e : list) {
            result.add(toEducationDTO(e));
        }

        return result;
    }

    public TeacherEducationDTO addEducation(
            Authentication auth,
            TeacherEducationDTO dto) {
        Teacher teacher = getTeacher(auth);

        TeacherEducation edu = new TeacherEducation();
        edu.setTeacher(teacher);
        edu.setInstitution(dto.getInstitution());
        edu.setDegree(dto.getDegree());
        edu.setMajor(dto.getMajor());
        edu.setStartDate(dto.getStartDate());
        edu.setEndDate(dto.getEndDate());

        educationRepository.save(edu);
        return toEducationDTO(edu);
    }

    public TeacherEducationDTO updateEducation(
            Authentication auth,
            Long educationId,
            TeacherEducationDTO updated) {
        Teacher teacher = getTeacher(auth);

        TeacherEducation edu = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!edu.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        edu.setInstitution(updated.getInstitution());
        edu.setDegree(updated.getDegree());
        edu.setMajor(updated.getMajor());
        edu.setStartDate(updated.getStartDate());
        edu.setEndDate(updated.getEndDate());

        educationRepository.save(edu);
        return toEducationDTO(edu);
    }

    public void deleteEducation(Authentication auth, Long educationId) {
        Teacher teacher = getTeacher(auth);

        TeacherEducation edu = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        if (!edu.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        educationRepository.delete(edu);
    }

    /* ================= EXPERIENCE ================= */

    public List<TeacherExperienceDTO> getMyExperiences(Authentication auth) {

        Teacher teacher = getTeacher(auth);
        List<TeacherExperience> list = experienceRepository.findByTeacher_Id(teacher.getId());

        List<TeacherExperienceDTO> result = new ArrayList<>();

        for (TeacherExperience e : list) {
            result.add(toExperienceDTO(e));
        }

        return result;
    }

    public TeacherExperienceDTO addExperience(
            Authentication auth,
            TeacherExperienceDTO dto) {
        Teacher teacher = getTeacher(auth);

        TeacherExperience exp = new TeacherExperience();
        exp.setTeacher(teacher);
        exp.setCompany(dto.getCompany());
        exp.setDesignation(dto.getDesignation());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());

        experienceRepository.save(exp);
        return toExperienceDTO(exp);
    }

    public TeacherExperienceDTO updateExperience(
            Authentication auth,
            Long experienceId,
            TeacherExperienceDTO updated) {
        Teacher teacher = getTeacher(auth);

        TeacherExperience exp = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (!exp.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        exp.setCompany(updated.getCompany());
        exp.setDesignation(updated.getDesignation());
        exp.setStartDate(updated.getStartDate());
        exp.setEndDate(updated.getEndDate());

        experienceRepository.save(exp);
        return toExperienceDTO(exp);
    }

    public void deleteExperience(Authentication auth, Long experienceId) {
        Teacher teacher = getTeacher(auth);

        TeacherExperience exp = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (!exp.getTeacher().getId().equals(teacher.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        experienceRepository.delete(exp);
    }

    /* ================= PUBLIC ================= */

    public List<MentorDTO> getAllMentors() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<MentorDTO> result = new ArrayList<>();

        for (Teacher t : teachers) {
            result.add(toMentorDTO(t));
        }

        return result;
    }

    private MentorDTO toMentorDTO(Teacher t) {
        MentorDTO dto = new MentorDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setEmail(t.getEmail());

        TeacherProfile profile = (TeacherProfile) teacherProfileRepository
                .findTeacherProfileByTeacher_Id(t.getId());

        if (profile != null) {
            dto.setBio(profile.getBio());
            dto.setField(profile.getField());
            dto.setExperienceYears(profile.getExperienceYears());
        }

        // Map experiences
        List<TeacherExperienceDTO> exps = new ArrayList<>();
        experienceRepository.findByTeacher_Id(t.getId())
                .forEach(e -> exps.add(toExperienceDTO(e)));
        dto.setExperiences(exps);

        // Course count
        // Assuming courseRepository is needed here. Let's add it to the service if not
        // there.
        // I checked earlier and teacherRepository, teacherProfileRepository,
        // educationRepository, experienceRepository are there.
        // Wait, I need CourseRepository too.
        dto.setCourseCount(courseRepository.findCourseByTeacher_id(t.getId()).size());
        return dto;
    }

    /* ================= MAPPERS ================= */

    private TeacherEducationDTO toEducationDTO(TeacherEducation e) {
        TeacherEducationDTO dto = new TeacherEducationDTO();
        dto.setId(e.getId());
        dto.setInstitution(e.getInstitution());
        dto.setDegree(e.getDegree());
        dto.setMajor(e.getMajor());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        return dto;
    }

    private TeacherExperienceDTO toExperienceDTO(TeacherExperience e) {
        TeacherExperienceDTO dto = new TeacherExperienceDTO();
        dto.setId(e.getId());
        dto.setCompany(e.getCompany());
        dto.setDesignation(e.getDesignation());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        return dto;
    }
}
