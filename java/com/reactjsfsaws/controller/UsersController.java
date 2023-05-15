package com.reactjsfsaws.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.reactjsfsaws.entity.Users;
import com.reactjsfsaws.repository.UsersRepository;
import com.reactjsfsaws.service.UsersService;

import jakarta.persistence.Id;




@RestController

@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/v0")
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Users users) {
		try {
			
			List<Users> duplicateNumber = usersRepository.findByPhoneNumber(users.getPhoneNumber());

			if (!duplicateNumber.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile Number already exists");

			}
			Users newUser = usersService.registerUser(users);
			return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
		} catch (HttpClientErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("Id") long Id,
                                       @RequestParam("file")MultipartFile file) {
		usersService.uploadUserProfileImage(Id, file);
    }
	@GetMapping("/login")
	public ResponseEntity<?> login(@RequestBody Users users) {
		try {
			
			List<Users> checkNumber = usersRepository.findByPhoneNumber(users.getPhoneNumber());
			List<Users> checkPass = usersRepository.findByPassword(users.getPassword());

			if (!checkNumber.isEmpty() && !checkPass.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK).body("Login Sucessfully");

			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Faild");
		} catch (HttpClientErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

	@GetMapping("/user/{Id}")
	public ResponseEntity<Users> getProfile(@PathVariable long Id) {
		try {
			Users user = usersService.getUser(Id);
			return ResponseEntity.ok(user);
		} catch (HttpClientErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GetMapping("/user")
	public ResponseEntity<List<Users>> getAllUser() {
		try {
			List<Users> allUser = usersService.getAllUser();
			return ResponseEntity.ok(allUser);
		} catch (HttpClientErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{Id}")
	public ResponseEntity<Users> updateProfile(@PathVariable long Id, @RequestBody Users user) {
		try {
			Optional<Users> existingUser = usersRepository.findById(Id);
			if (existingUser.isPresent()) {
				user.setId(Id);
				usersRepository.save(user);
				return new ResponseEntity<>(user, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (HttpClientErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/{Id}")
	public ResponseEntity<Users> deleteProfile(@PathVariable long Id) {
		try {
			Optional<Users> existingUser = usersRepository.findById(Id);
			if (existingUser.isPresent()) {
				usersRepository.deleteById(Id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (HttpClientErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (HttpServerErrorException ex) {
			
			return ResponseEntity.status(ex.getStatusCode()).build();
		} catch (Exception ex) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
//	 @GetMapping("{userProfileId}/image/download")
//	    public byte[] downloadUserProfileImage(@PathVariable("Id")long Id) {
//	        return usersService.downloadUserProfileImage(Id);
//	    }

	
}
