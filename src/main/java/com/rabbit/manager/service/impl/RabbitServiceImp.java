package com.rabbit.manager.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.rabbit.manager.dto.RabbitDTO;
import com.rabbit.manager.model.Rabbit;
import com.rabbit.manager.repository.RabbitRepository;
import com.rabbit.manager.service.FileUploadService;
import com.rabbit.manager.service.RabbitService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RabbitServiceImp implements RabbitService{

	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	RabbitRepository rabbitRepository;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Override
	public RabbitDTO addRabbit(RabbitDTO rabbitDTO) {
		return convertEntityToDto(rabbitRepository.save(convertDtoToEntity(rabbitDTO)));
	}

	@Override
	public RabbitDTO updateRabbit(RabbitDTO rabbitDTO) {
		return convertEntityToDto(rabbitRepository.save(convertDtoToEntity(rabbitDTO)));
	}

	@Override
	public List<RabbitDTO> allRabbit() {
		return rabbitRepository.findAll()
				.stream()
				.map(this::convertEntityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public RabbitDTO getById(String id) {
		return convertEntityToDto(rabbitRepository.findById(id).get());
	}

	@Override
	public void deleteRabbit(String id) {
		try {
			Rabbit r = rabbitRepository.findById(id).get();
			fileUploadService.DeleteFile(r.getImagePath());
		    rabbitRepository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public RabbitDTO convertEntityToDto(Rabbit rabbit) {
		return modelMapper.map(rabbit, RabbitDTO.class);
	}

	@Override
	public Rabbit convertDtoToEntity(RabbitDTO rabbitDTO) {
		return modelMapper.map(rabbitDTO, Rabbit.class);
	}

	@Override
	public RabbitDTO softDelete(RabbitDTO rabbitDTO) {
		return convertEntityToDto(rabbitRepository.save(convertDtoToEntity(rabbitDTO)));
	}

	@Override
	public Long countRabbit() {
		return rabbitRepository.count();
	}

}
