package com.rabbit.manager.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.rabbit.manager.model.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
	
	Optional<Role> findByName(String name);

}
