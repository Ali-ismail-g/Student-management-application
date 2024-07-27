package com.app.studentManagementSystem.controllers;

import com.app.studentManagementSystem.entity.Student;
import com.app.studentManagementSystem.model.request.StudentRequest;
import com.app.studentManagementSystem.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/students")
@Validated
@Tag(name="Student management APIs",description = "APIs for managing students by Admin access only and student has an access to view info only")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping(value="")
    @Operation(summary = "Create new student",description = "this API will create new student by access of Admin only")
    @Parameter(name = "StudentRequest & JwtToken",description = "it consists of token to check role of Admin to create a new record of student",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<Student> save(@Valid @RequestBody StudentRequest studentRequest, @RequestHeader("Authorization")String token) {
        studentRequest.setJwtToken(token);
        return ResponseEntity.ok(studentService.save(studentRequest));
    }
    @PutMapping(value="")
    @Operation(summary = "Update a student",description = "this API will update an existing student by access of Admin only")
    @Parameter(name = "StudentRequest, studentId & JwtToken",description = "it consists of token to check role of Admin and studentId to update specific record of student",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<Student> update(@Valid @RequestBody StudentRequest studentRequest,@RequestHeader("Authorization")String token,@RequestParam("id") long id){
        studentRequest.setJwtToken(token);
        return ResponseEntity.ok(studentService.update(id,studentRequest));
    }
    @GetMapping(value = "")
    @Operation(summary = "Get all student",description = "this API will get all stored students by access of Admin only")
    @Parameter(name = "JwtToken",description = "it consists of token to check role of Admin to get all records",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<List<Student>> getAllStudents(@Valid @RequestHeader("Authorization")String token){
        return ResponseEntity.ok(studentService.findAll(token));
    }
    @DeleteMapping(value="")
    @Operation(summary = "Delete a student",description = "this API will delete an existing student by access of Admin only")
    @Parameter(name = "JwtToken & studentId",description = "it consists of token to check role of Admin and id of student to delete his recorde",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<String> deleteStudent(@Valid @RequestHeader("Authorization")String token,@RequestParam("id") long id){
        return ResponseEntity.ok(studentService.deleteById(id,token));
    }
    @GetMapping(value = "/student")
    @Operation(summary = "Get a student info",description = "this API will get any student info using id of already created student by Admin by access of Student only")
    @Parameter(name = "JwtToken & studentId",description = "it consists of token to check role of Student and id of student to get his info",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<Optional<Student>> findStudent(@Valid @RequestHeader("Authorization")String token, @RequestParam("id") long id){
        return ResponseEntity.ok(studentService.findById(id,token));
    }
}
