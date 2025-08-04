package com.moradiasestudantis.gestao_moradias.controller;

import com.moradiasestudantis.gestao_moradias.dto.StudentFilterDto;
import com.moradiasestudantis.gestao_moradias.dto.StudentDto;
import com.moradiasestudantis.gestao_moradias.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

@GetMapping("/search")
public ResponseEntity<List<StudentDto>> searchStudents(@ModelAttribute StudentFilterDto filter) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_PROPRIETARIO"))) {
        return ResponseEntity.status(403).build();
    }

    List<StudentDto> students = studentService.searchStudents(filter);
    return ResponseEntity.ok(students);
}

}