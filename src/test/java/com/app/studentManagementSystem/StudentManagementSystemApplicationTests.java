package com.app.studentManagementSystem;

import com.app.studentManagementSystem.entity.Role;
import com.app.studentManagementSystem.entity.Student;
import com.app.studentManagementSystem.model.request.LoginRequest;
import com.app.studentManagementSystem.model.request.RegisterRequest;
import com.app.studentManagementSystem.model.request.StudentRequest;
import com.app.studentManagementSystem.model.response.LoginResponse;
import com.app.studentManagementSystem.model.response.RegisterResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentManagementSystemApplicationTests {
	@Autowired
	private RestTemplate restTemplate;
	private String jwtToken;

//	@Test
//	void contextLoads() {
//	}
	@BeforeEach
	public void obtainJwtToken(TestInfo testInfo){
		if (!testInfo.getTestMethod().get().getName().equals("testRegister")) {
			String url = "http://localhost:8080/rest/auth/login";
			LoginRequest loginRequest = new LoginRequest();
			loginRequest.setEmail("mahsdy@gmail.com");
			loginRequest.setPassword("Abcd!234");
			HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest);
			ResponseEntity<LoginResponse> responseEntity = restTemplate.postForEntity(url,entity, LoginResponse.class);
			jwtToken = responseEntity.getBody().getToken();
			System.out.println(jwtToken);
		}
	}
	@Test
	@Order(1)
	public void testRegister(){
		String url = "http://localhost:8080/rest/auth/register";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setUserName("omarMahdy");
		registerRequest.setPassword("Abcd!234");
		registerRequest.setEmail("mahsdy@gmail.com");
		registerRequest.setRole(Role.admin);
		HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest);

		try{
			ResponseEntity<RegisterResponse> responseEntity = restTemplate.postForEntity(url,entity, RegisterResponse.class);
			System.out.println("res entity: "+responseEntity);
			assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
			assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getSuccessMessage());
		}catch (HttpServerErrorException e) {
			System.out.println("HTTP Status Code: " + e.getStatusCode());
			System.out.println("Response Body: " + e.getResponseBodyAsString());
			throw e;
		}
	}
	@Test
	@Order(2)
	public void testLogin(){
		String url = "http://localhost:8080/rest/auth/login";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("mahsdy@gmail.com");
		loginRequest.setPassword("Abcd!234");
		HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest);
		ResponseEntity<LoginResponse> responseEntity = restTemplate.postForEntity(url,entity, LoginResponse.class);
		System.out.println("res "+responseEntity);
//		jwtToken = responseEntity.getBody().getToken();
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody().getToken());
	}

	@Test
	@Order(3)
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
			assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getEmail());
		} catch (HttpServerErrorException e) {
			System.out.println("HTTP Status Code: " + e.getStatusCode());
			System.out.println("Response Body: " + e.getResponseBodyAsString());
			throw e;
		}
	}
	@Test
	@Order(4)
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
	@Order(5)
	public void getStudents(){
		String url = "http://localhost:8080/api/students";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization","Bearer "+jwtToken);
		HttpEntity<Void> headersRequest = new HttpEntity<>(headers);
		ResponseEntity<List> responseEntity = restTemplate.exchange(url, HttpMethod.GET,headersRequest ,List.class);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
	}
	@Test
	@Order(6)
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
	@Order(7)
	public void getStudent(){
		String url = "http://localhost:8080/rest/auth/register";
		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setEmail("zizo97@gmail.com");
		registerRequest.setRole(Role.student);
		registerRequest.setPassword("aBc2#233");
		registerRequest.setUserName("omarZizo");
		HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest);
		ResponseEntity<RegisterResponse> responseEntity = restTemplate.postForEntity(url,entity, RegisterResponse.class);

		String url2 = "http://localhost:8080/rest/auth/login";
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("zizo97@gmail.com");
		loginRequest.setPassword("aBc2#233");
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
