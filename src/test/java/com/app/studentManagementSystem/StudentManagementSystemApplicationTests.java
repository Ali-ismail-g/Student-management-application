package com.app.studentManagementSystem;

import com.app.studentManagementSystem.entity.Role;
import com.app.studentManagementSystem.entity.Student;
import com.app.studentManagementSystem.model.request.LoginRequest;
import com.app.studentManagementSystem.model.request.RegisterRequest;
import com.app.studentManagementSystem.model.request.StudentRequest;
import com.app.studentManagementSystem.model.response.LoginResponse;
import com.app.studentManagementSystem.model.response.RegisterResponse;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class StudentManagementSystemApplicationTests {
	@Autowired
	private RestTemplate restTemplate;
	private String jwtToken;

	@Test
	void contextLoads() {
	}
	@Test
	public void testRegister(){
		String url = "http://localhost:8080/rest/auth/register";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("aliesmail94@gmail.com");
		registerRequest.setRole(Role.admin);
		registerRequest.setPassword("99999");
		registerRequest.setUserName("aliIsmail");

		HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest);
		ResponseEntity<RegisterResponse> responseEntity = restTemplate.postForEntity(url,entity, RegisterResponse.class);
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getSuccessMessage());
	}
	@Test
	public void testLogin(){
		String url = "http://localhost:8080/rest/auth/login";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("aliesmail94@gmail.com");
		loginRequest.setPassword("99999");
		HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest);
		ResponseEntity<LoginResponse> responseEntity = restTemplate.postForEntity(url,entity, LoginResponse.class);
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getToken());
	}
	@BeforeEach
	public void obtainJwtToken(){
		String url = "http://localhost:8080/rest/auth/login";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("aliesmail94@gmail.com");
		loginRequest.setPassword("99999");
		HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest);
		ResponseEntity<LoginResponse> responseEntity = restTemplate.postForEntity(url,entity, LoginResponse.class);
		jwtToken = responseEntity.getBody().getToken();
		System.out.println(jwtToken);
	}
	@Test
	public void createStudent() throws ParseException {
		String url = "http://localhost:8080/api/students";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer "+jwtToken);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StudentRequest studentRequest = new StudentRequest();
		studentRequest.setJwtToken(jwtToken);
		studentRequest.setEmail("zizo97@gmail.com");
		studentRequest.setDateOfBirth(simpleDateFormat.parse("2024-11-23"));
		studentRequest.setFirstName("omar");
		studentRequest.setLastName("zizo");
		HttpEntity<StudentRequest> entity = new HttpEntity<>(studentRequest,headers);
		try {
			ResponseEntity<Student> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Student.class);
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertNotNull(responseEntity.getBody().getEmail());
		} catch (HttpServerErrorException e) {
			System.out.println("HTTP Status Code: " + e.getStatusCode());
			System.out.println("Response Body: " + e.getResponseBodyAsString());
			throw e;
		}
	}
	@Test
	public void updateStudent() throws ParseException {
		String url = "http://localhost:8080/api/students?id=1";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer "+jwtToken);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StudentRequest studentRequest = new StudentRequest();
		studentRequest.setJwtToken(jwtToken);
		studentRequest.setEmail("mahdy99@gmail.com");
		studentRequest.setDateOfBirth(simpleDateFormat.parse("2021-12-20"));
		studentRequest.setFirstName("taha");
		studentRequest.setLastName("mahdy");
		HttpEntity<StudentRequest> entity = new HttpEntity<>(studentRequest,headers);
		try {
			ResponseEntity<Student> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, Student.class);
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertNotNull(responseEntity.getBody().getEmail());
		} catch (HttpServerErrorException e) {
			System.out.println("HTTP Status Code: " + e.getStatusCode());
			System.out.println("Response Body: " + e.getResponseBodyAsString());
			throw e;
		}
	}
	@Test
	public void getStudents(){
		String url = "http://localhost:8080/api/students";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer "+jwtToken);
		HttpEntity<Void> headersRequest = new HttpEntity<>(headers);
		ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.GET,headersRequest ,List.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}
	@Test
	public void deleteStudent(){
		String url = "http://localhost:8080/api/students?id=1";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer "+jwtToken);
		HttpEntity<Void> headersRequest = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE,headersRequest, String.class);
			assertEquals(responseEntity.getStatusCode(),HttpStatus.OK);
		}catch (HttpServerErrorException e){
			System.out.println("HTTP Status Code: " + e.getStatusCode());
			System.out.println("Response Body: " + e.getResponseBodyAsString());
			throw e;
		}
	}
	@Test
	public void getStudent(){
		String url = "http://localhost:8080/rest/auth/register";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("zizo97@gmail.com");
		registerRequest.setRole(Role.student);
		registerRequest.setPassword("99999");
		registerRequest.setUserName("omarZizo");
		HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest);
		ResponseEntity<RegisterResponse> responseEntity = restTemplate.postForEntity(url,entity, RegisterResponse.class);

		String url2 = "http://localhost:8080/rest/auth/login";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("zizo97@gmail.com");
		loginRequest.setPassword("99999");
		HttpEntity<LoginRequest> entity2 = new HttpEntity<>(loginRequest);
		ResponseEntity<LoginResponse> responseEntity2 = restTemplate.postForEntity(url2,entity2, LoginResponse.class);
		String token=responseEntity2.getBody().getToken();

		String url3 = "http://localhost:8080/api/students/student?id=1";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer "+token);
		HttpEntity<Void> headersRequest = new HttpEntity<>(headers);
		ResponseEntity<Student> responseEntity3 = restTemplate.exchange(url3, HttpMethod.GET,headersRequest ,Student.class);
		assertEquals(responseEntity3.getStatusCode(), HttpStatus.OK);
	}

}
