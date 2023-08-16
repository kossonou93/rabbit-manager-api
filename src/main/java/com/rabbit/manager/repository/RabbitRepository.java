package com.rabbit.manager.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.rabbit.manager.model.Rabbit;

public interface RabbitRepository extends MongoRepository<Rabbit, String> {
	List<Rabbit> findByName(String firstName);
}
