package com.example.lms.service;

import com.example.lms.config.JwtService;
import com.example.lms.dto.LoginRequestDTO;
import com.example.lms.dto.LoginResponseDTO;
import com.example.lms.dto.RegisterRequestDTO;
import com.example.lms.dto.RegisterResponseDTO;
import com.example.lms.entity.Admin;
import com.example.lms.entity.Student;
import com.example.lms.entity.Teacher;
import com.example.lms.repository.AdminRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtUtil;

    public RegisterResponseDTO register(RegisterRequestDTO request) {

        String role = request.getRole().toUpperCase();
        if ("ADMIN".equals(role)) {
            throw new RuntimeException("Admin registration is not allowed");
        }
        if ("STUDENT".equals(role)) {

            if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            Student student = new Student();
            student.setName(request.getName());
            student.setEmail(request.getEmail());
            student.setPassword(
                    passwordEncoder.encode(request.getPassword()));
            student.setEnabled(true);

            Student saved = studentRepository.save(student);

            return new RegisterResponseDTO(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    "STUDENT");
        }
        if ("TEACHER".equals(role)) {

            if (teacherRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }

            Teacher teacher = new Teacher();
            teacher.setName(request.getName());
            teacher.setEmail(request.getEmail());
            teacher.setPassword(
                    passwordEncoder.encode(request.getPassword()));
            teacher.setEnabled(true);

            Teacher saved = teacherRepository.save(teacher);

            return new RegisterResponseDTO(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    "TEACHER");
        }

        throw new RuntimeException("Invalid role");
    }

    public LoginResponseDTO login(LoginRequestDTO request) {

        String role = request.getRole().toUpperCase();

        if ("ADMIN".equals(role)) {
            Admin admin = adminRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            validatePassword(request.getPassword(), admin.getPassword());

            String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");

            return new LoginResponseDTO(
                    admin.getAdId(),
                    admin.getName(),
                    admin.getEmail(),
                    "ADMIN",
                    token);
        }

        if ("TEACHER".equals(role)) {
            Teacher teacher = teacherRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            if (!teacher.isEnabled()) {
                throw new RuntimeException("Account is disabled");
            }

            validatePassword(request.getPassword(), teacher.getPassword());

            String token = jwtUtil.generateToken(teacher.getEmail(), "TEACHER");

            return new LoginResponseDTO(
                    teacher.getId(),
                    teacher.getName(),
                    teacher.getEmail(),
                    "TEACHER",
                    token);
        }

        if ("STUDENT".equals(role)) {
            Student student = studentRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            if (!student.isEnabled()) {
                throw new RuntimeException("Account is disabled");
            }

            validatePassword(request.getPassword(), student.getPassword());

            String token = jwtUtil.generateToken(student.getEmail(), "STUDENT");

            return new LoginResponseDTO(
                    student.getId(),
                    student.getName(),
                    student.getEmail(),
                    "STUDENT",
                    token);
        }

        throw new RuntimeException("Invalid role");
    }

    private void validatePassword(String raw, String encoded) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw new RuntimeException("Invalid password");
        }
    }

    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email)
                || teacherRepository.existsByEmail(email)
                || adminRepository.existsByEmail(email);
    }
}
