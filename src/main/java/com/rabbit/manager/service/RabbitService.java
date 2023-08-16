package com.rabbit.manager.service;

import java.util.List;
import com.rabbit.manager.dto.RabbitDTO;
import com.rabbit.manager.model.Rabbit;

public interface RabbitService {

	RabbitDTO addRabbit(RabbitDTO rabbitDTO);
	RabbitDTO updateRabbit(RabbitDTO rabbitDTO);
	List<RabbitDTO> allRabbit();
	RabbitDTO getById(String id);
	void deleteRabbit(String id);
	RabbitDTO softDelete(RabbitDTO rabbitDTO);
	RabbitDTO convertEntityToDto(Rabbit rabbit);
	Rabbit convertDtoToEntity(RabbitDTO rabbitDTO);
	Long countRabbit();
}
