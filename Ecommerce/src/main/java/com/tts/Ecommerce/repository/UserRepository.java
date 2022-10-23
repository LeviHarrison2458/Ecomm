package com.tts.Ecommerce.repository;

import org.springframework.data.repository.CrudRepository;

import com.tts.Ecommerce.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);
}
