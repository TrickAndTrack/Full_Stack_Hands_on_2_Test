package com.reactjsfsaws.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reactjsfsaws.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByFirstName(String firstName);

	List<Users> findByPhoneNumber(String phoneNumber);

	List<Users> findByPassword(String password);

}
