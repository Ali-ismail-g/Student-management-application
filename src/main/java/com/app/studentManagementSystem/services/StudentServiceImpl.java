package com.app.studentManagementSystem.services;

import com.app.studentManagementSystem.entity.Student;
import com.app.studentManagementSystem.exception.RoleException;
import com.app.studentManagementSystem.exception.UserNotFoundException;
import com.app.studentManagementSystem.model.request.StudentRequest;
import com.app.studentManagementSystem.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public Student save(StudentRequest studentRequest){
        String jwtToken = JwtService.extractToken(studentRequest.getJwtToken());
        String role = JwtService.getRoleFromToken(jwtToken);
        if(!role.equals("admin")){
            throw new RoleException("Needs only Admin access!!");
        }
        Student student = Student.builder()
                .firstName(studentRequest.getFirstName())
                .lastName(studentRequest.getLastName())
                .email(studentRequest.getEmail())
                .dateOfBirth(studentRequest.getDateOfBirth())
                .build();
        Student savedStudent = studentRepository.save(student);
        System.out.println("A new student has been saved successfully!!");
        return savedStudent;
    }

    @Override
    public List<Student> findAll(String jwtToken) {
        String token = JwtService.extractToken(jwtToken);
        String role = JwtService.getRoleFromToken(token);
        if(!role.equals("admin")){
            throw new RoleException("Needs only Admin access!!");
        }
        return studentRepository.findAll();
    }

    @Override
    public String deleteById(long id, String jwtToken) {
        String token = JwtService.extractToken(jwtToken);
        String role = JwtService.getRoleFromToken(token);
        if(!role.equals("admin")){
            throw new RoleException("Needs only Admin access!!");
        }
        Student student = studentRepository.getOne(id);
        studentRepository.delete(student);
        return "A student has been Removed successfully!!";
    }

    @Override
    public Student update(long id,StudentRequest studentRequest) {
        String jwtToken = JwtService.extractToken(studentRequest.getJwtToken());
        String role = JwtService.getRoleFromToken(jwtToken);
        if(!role.equals("admin")){
            throw new RoleException("Needs only Admin access!!");
        }
        Student student = studentRepository.getOne(id);
        student.setEmail(studentRequest.getEmail());
        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setDateOfBirth(studentRequest.getDateOfBirth());
        System.out.println("A student has been updated successfully!!");
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findById(long id, String jwtToken) {
        String token = JwtService.extractToken(jwtToken);
        String role = JwtService.getRoleFromToken(token);
        if(!role.equals("student")){
            throw new RoleException("Needs only Student access!!");
        }
        Optional<Student> student =studentRepository.findById(id);
        if(student==null){
            throw new UserNotFoundException("Student does not exist!!");
        }
        return student;
    }


}
