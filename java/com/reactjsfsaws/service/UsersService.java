package com.reactjsfsaws.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.reactjsfsaws.entity.Users;

import jakarta.persistence.Id;

public interface UsersService {

	Users registerUser(Users user);

	List<Users> getAllUser();

	Users getUser(long id);

	

	void uploadUserProfileImage(long Id, MultipartFile file);

//	byte[] downloadUserProfileImage(long Id);

}
