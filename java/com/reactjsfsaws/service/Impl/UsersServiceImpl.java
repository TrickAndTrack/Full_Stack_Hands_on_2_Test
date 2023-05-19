package com.reactjsfsaws.service.Impl;

import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

import java.io.IOException;
import java.util.Arrays;

import java.util.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.reactjsfsaws.bucket.BucketName;
import com.reactjsfsaws.entity.Users;
import com.reactjsfsaws.exception.ResourceNotFoundException;
import com.reactjsfsaws.repository.UsersRepository;
import com.reactjsfsaws.service.UsersService;
import com.reactjsfsaws.service.Filestore.FileStore;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersRepository usersRepository;

	private Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

	@Override
	public Users registerUser(Users user) {
		logger.info("registation started");
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String Encryptopass = bcrypt.encode(user.getPassword());
		user.setCreatedBy(user.getFirstName());
		user.setRegisterDate(new Date());
		user.setPassword(Encryptopass);
		return usersRepository.save(user);
	}

	@Override
	public Users updateUser(Users user) {
		logger.info("update registation started");
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String Encryptopass = bcrypt.encode(user.getPassword());
		user.setUpdatedBy(user.getFirstName());
		user.setUpdatedDate(new Date());

		user.setPassword(Encryptopass);
		return usersRepository.save(user);
	}

	@Override
	public Users getUser(long id) {
		logger.info("User geting started");

		Users user = usersRepository.findById(null).orElseThrow(
				() -> new ResourceNotFoundException("User with given id is not found on server !! : " + id));

		return user;
	}

	@Override
	public List<Users> getAllUser() {
		logger.info("List Of Users geting started");
		return usersRepository.findAll();
	}

	@Override
	public void uploadUserProfileImage(long Id, MultipartFile file) {
		isFileEmpty(file);

		isImage(file);

		Users user = getUser(Id);

		Map<String, String> metadata = extractMetadata(file);

		String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId());
		String filename = String.format("%s-%s", file.getOriginalFilename(), Id);

		try {
			FileStore fileStore = new FileStore(null);
			fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
			user.setProfileImage(filename);
			usersRepository.save(user);

		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

//	@Override
//	public byte[] downloadUserProfileImage(long Id) {
//        Users user = getUser(Id);
//        String path = String.format("%s/%s",
//                BucketName.PROFILE_IMAGE.getBucketName(),
//                user.getId());
//
//        return user.getProfileImage().Map(key -> fileStore.download(path, key)).orElse(new byte[0]);
//    }

	private Map<String, String> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}

	private void isImage(MultipartFile file) {
		if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_GIF.getMimeType(), IMAGE_PNG.getMimeType())
				.contains(file.getContentType())) {
			throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
		}
	}

	private void isFileEmpty(MultipartFile file) {
		if (file.isEmpty())
			throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
	}

//	    public boolean isPasswordValid(String hashedPassword, String inputPassword) {
//	    	BCryptPasswordEncoder bcrypt= new BCryptPasswordEncoder();
//	        return bcrypt.matches(inputPassword, hashedPassword);
//	    }

}
