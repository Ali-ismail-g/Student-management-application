package com.app.studentManagementSystem.services;

import com.app.studentManagementSystem.entity.Student;
import com.app.studentManagementSystem.model.request.StudentRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {
    public Student save(StudentRequest studentRequest);
    public List<Student> findAll(String jwtToken);
    public String deleteById(long id,String jwtToken);
    public Student update(long id,StudentRequest studentRequest);
    public Optional<Student> findById(long id,String jwtToken);
}
